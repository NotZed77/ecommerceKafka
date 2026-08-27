package com.notzed.notification.service.consumer;

import com.notzed.notification.service.event.OrderCreatedEvent;
import com.notzed.notification.service.event.OrderStatusUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@Configuration
public class OrderKafkaConsumer {

    @KafkaListener(topics = "order-created-topic")
    public void handleOrderCreatedTopic(OrderStatusUpdatedEvent orderStatusUpdatedEvent){
        log.info("HandleOrderCreated: {}", orderStatusUpdatedEvent);
    }

    @KafkaListener(topics = "order-status-updated-topic", groupId = "inventory-service-group")
    public void handleOrderUpdatedStatusTopic1(OrderStatusUpdatedEvent orderStatusUpdatedEvent){
        log.info("HandleOrderUpdated: {}", orderStatusUpdatedEvent);
    }

    @KafkaListener(topics = "order_status-updated-topic")
    public void handleOrderStatusTopic(OrderStatusUpdatedEvent orderStatusUpdatedEvent){
        log.info("HandleOrderStatus: {}", orderStatusUpdatedEvent);
    }
}
