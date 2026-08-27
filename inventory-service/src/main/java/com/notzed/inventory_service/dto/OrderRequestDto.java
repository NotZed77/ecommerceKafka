package com.notzed.inventory_service.dto;

import com.notzed.inventory_service.entity.OrderStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class OrderRequestDto {
    private String orderId;
    private List<OrderItemRequestDto> items;

    public OrderRequestDto(String orderId, List<OrderItemRequestDto> items){
        this.orderId = orderId;
        this.items = items;
    }
}
