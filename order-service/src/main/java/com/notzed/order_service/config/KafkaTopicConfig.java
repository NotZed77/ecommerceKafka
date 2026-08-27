package com.notzed.order_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topic.order-created-topic}")
    public String KAFKA_ORDER_CREATED_TOPIC;

    @Value("${kafka.topic.order-status-updated-topic}")
    public String KAFKA_ORDER_STATUS_UPDATED_TOPIC;

    @Bean
    public NewTopic orderCreatedTopic(){
        return new NewTopic(KAFKA_ORDER_CREATED_TOPIC, 3, (short) 1);
    }

    @Bean
    public NewTopic orderUpdatedTopic(){
        return new NewTopic(KAFKA_ORDER_STATUS_UPDATED_TOPIC, 1, (short) 1);
    }
}
