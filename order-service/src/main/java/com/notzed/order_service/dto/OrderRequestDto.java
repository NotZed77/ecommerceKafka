package com.notzed.order_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {
    private List<OrderItemRequestDto> items;
}
