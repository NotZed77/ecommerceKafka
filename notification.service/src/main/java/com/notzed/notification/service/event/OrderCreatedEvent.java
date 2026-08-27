package com.notzed.notification.service.event;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Data
@Getter
@Setter
public class OrderCreatedEvent {
    private String orderId;
    private String reason;
    private Instant updatedAt;
}

