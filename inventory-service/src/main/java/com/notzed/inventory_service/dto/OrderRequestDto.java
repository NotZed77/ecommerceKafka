package com.notzed.inventory_service.dto;

import com.notzed.inventory_service.entity.OrderStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
public class OrderRequestDto {
    private Long id;
    private List<OrderItemRequestDto> items;
    private Integer totalPrice;

    public OrderRequestDto(Long id, List<OrderItemRequestDto> items){
        this.id = id;
        this.items = items;
    }
}
