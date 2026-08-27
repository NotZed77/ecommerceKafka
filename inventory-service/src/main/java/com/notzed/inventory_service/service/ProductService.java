package com.notzed.inventory_service.service;

import com.notzed.inventory_service.consumer.OrdersKafkaConsumer;
import com.notzed.inventory_service.dto.OrderItemRequestDto;
import com.notzed.inventory_service.dto.OrderRequestDto;
import com.notzed.inventory_service.dto.ProductDto;
import com.notzed.inventory_service.entity.OrderStatus;
import com.notzed.inventory_service.entity.Product;
import com.notzed.inventory_service.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<Long, OrderStatus> kafkaTemplate;
    private final OrdersKafkaConsumer inventoryKafkaConsumer;

    private static final String ORDER_STATUS_TOPIC = "order_status-updated";
    private static final String ORDER_STATUS_TOPIC2 = "order_status-updated2";

    public ProductDto getProductById(Long productId){
        log.info("Fetching product with ID: {}", productId);
        Optional<Product> product = productRepository.findById(productId);
        return modelMapper.map(product, ProductDto.class);
    }

    public List<ProductDto> getAllProducts(){
        log.info("Fetching all the items");
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map((element) -> modelMapper.map(element, ProductDto.class)).toList();
    }

    @Transactional
    public Double reduceStocks(OrderRequestDto event){
        log.info("Reducing the stocks for id: {}", event.getId());
        if(event.getItems() == null || event.getItems().isEmpty()){
            throw new RuntimeException("Order items can not be empty");
        }

        List<Product> productsToUpdate = new ArrayList<>();
        for(OrderItemRequestDto orderItemRequestDto: event.getItems()){
            Long productId = orderItemRequestDto.getProductId();
            Integer quantity = orderItemRequestDto.getQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: "+ productId));

            if(product.getStock() < orderItemRequestDto.getQuantity()){
                kafkaTemplate.send(ORDER_STATUS_TOPIC, event.getId(), OrderStatus.OUT_OF_STOCK);
                throw new RuntimeException("Insufficient stock for product: "+ orderItemRequestDto.getProductId());
            }

        }
        double totalPrice = 0.0;
        for(int i=0; i < productsToUpdate.size(); i++){
            Product product = productsToUpdate.get(i);
            int quantity = event.getItems().get(i).getQuantity();

            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
            totalPrice += product.getPrice() * quantity;
        }
        kafkaTemplate.send(ORDER_STATUS_TOPIC, event.getId(), OrderStatus.FULFILLED);

        return totalPrice;
    }



}
