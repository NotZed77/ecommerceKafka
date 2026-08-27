package com.notzed.order_service.consumer;

import com.notzed.order_service.event.OrderStatusUpdatedEvent;
import com.notzed.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OrderKafkaConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "order_status-updated-topic")
    public void handleOrderStatusTopic(OrderStatusUpdatedEvent orderStatusUpdatedEvent){
        log.info("HandleOrderStatus: {}", orderStatusUpdatedEvent);
    }
}
