package com.notzed.order_service.event;

import com.notzed.order_service.entity.OrderStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Data
@Getter
@Setter
public class OrderCreatedEvent {
    private String orderId;
    private OrderStatus status;
    private String reason;
    private Instant updatedAt;
}

