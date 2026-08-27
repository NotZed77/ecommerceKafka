package com.notzed.inventory_service.consumer;

import com.notzed.inventory_service.event.OrderStatusUpdatedEvent;
import com.notzed.inventory_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@Configuration
public class OrdersKafkaConsumer {

    @KafkaListener(topics = "order-created-topic")
    public void handleOrderCreatedTopic(OrderStatusUpdatedEvent orderStatusUpdatedEvent){
        log.info("HandleOrderCreated: {}", orderStatusUpdatedEvent);

    }

    @KafkaListener(topics = "order-status-updated-topic", groupId = "inventory-service-group")
    public void handleOrderUpdatedStatusTopic1(OrderStatusUpdatedEvent orderStatusUpdatedEvent){
        log.info("HandleOrderUpdated: {}", orderStatusUpdatedEvent);
    }
}

