package com.notzed.inventory_service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class OrderItemRequestDto {
    private Long productId;
    private Integer quantity;
}
