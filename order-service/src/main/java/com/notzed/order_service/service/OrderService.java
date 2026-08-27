package com.notzed.order_service.service;

import com.notzed.order_service.client.InventoryOpenFeignClient;
import com.notzed.order_service.dto.OrderRequestDto;
import com.notzed.order_service.entity.Order;
import com.notzed.order_service.entity.OrderItem;
import com.notzed.order_service.entity.OrderStatus;
import com.notzed.order_service.event.OrderCreatedEvent;
import com.notzed.order_service.event.OrderStatusUpdatedEvent;
import com.notzed.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    @Value("${kafka.topic.order-created-topic}")
    public String KAFKA_ORDER_CREATED_TOPIC;

    @Value("${kafka.topic.order-status-updated-topic}")
    public String KAFKA_ORDER_STATUS_UPDATED_TOPIC;

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    private final KafkaTemplate<String, OrderStatusUpdatedEvent> kafkaTemplate2;
    private final InventoryOpenFeignClient inventoryOpenFeignClient;

    public List<OrderRequestDto> getAllOrders(){
        log.info("Fetching all orders");
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map((element) -> modelMapper.map(element, OrderRequestDto.class)).toList();
    }

    public OrderRequestDto getOrderById(Long id) {
        log.info("Fetching order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: {}" + id));
        return modelMapper.map(order, OrderRequestDto.class);
    }

    public void createNewOrder(OrderRequestDto orderRequestDto){
        Double totalPrice = reduceStocks(orderRequestDto);
        Order order = modelMapper.map(orderRequestDto, Order.class);
        log.info("Calling the createOrder Method");

        for(OrderItem item: order.getItems()){
            item.setOrder(order);
            totalPrice += item.getPrice() * item.getQuantity();
        }

        order.setTotalPrice(totalPrice);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent orderCreatedEvent = modelMapper.map(savedOrder, OrderCreatedEvent.class);
        kafkaTemplate.send(KAFKA_ORDER_CREATED_TOPIC, orderCreatedEvent.getOrderId(), orderCreatedEvent);
    }

    private Double reduceStocks(OrderRequestDto orderRequestDto) {
        return inventoryOpenFeignClient.reduceStocks(orderRequestDto);
    }

    @Transactional
    public void saveOrderStatus(Long id){
        log.info("Fetching the order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
        order.setOrderStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        OrderStatusUpdatedEvent event = modelMapper.map(order, OrderStatusUpdatedEvent.class);
        kafkaTemplate2.send(KAFKA_ORDER_STATUS_UPDATED_TOPIC, event.getOrderId(), event)
                .whenComplete((result, ex) -> {
                    if(ex != null){
                        log.error("Failed to publish status update for order {}", id, ex);
                    }
                });
    }


}
