package com.notzed.order_service.controller;

import com.notzed.order_service.dto.OrderDto;
import com.notzed.order_service.dto.OrderRequestDto;
import com.notzed.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderRequestDto>> getAllOrders(){
        List<OrderRequestDto> orders =orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderRequestDto> getOrderById(@PathVariable Long orderId){
        OrderRequestDto orderRequestDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderRequestDto);
    }

    @PostMapping("/create-order")
    public ResponseEntity<String> createNewOrder(@RequestBody OrderRequestDto orderRequestDto){
        orderService.createNewOrder(orderRequestDto);
        return ResponseEntity.ok("Order is created");
    }
}
