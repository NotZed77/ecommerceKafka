package com.notzed.order_service.dto;

import com.notzed.order_service.entity.OrderItem;
import com.notzed.order_service.entity.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {

    private Long orderId;
    private Long productId;
    private Integer quantity;
}
