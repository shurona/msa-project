package com.sparta.msa_exam.order.controller;

import com.sparta.msa_exam.order.dto.CreateOrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.dto.UpdateOrderRequestDto;
import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.entity.OrderProduct;
import com.sparta.msa_exam.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("order")
public class OrderApiController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody CreateOrderRequestDto requestDto) {
        Long orders = orderService.createOrders(requestDto.getName(), requestDto.getProduct_ids());
        return new ResponseEntity<>(orders, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Long> addProductToOrder(
            @PathVariable("orderId") Long orderId,
            @RequestBody UpdateOrderRequestDto requestDto
    ) {

        OrderResponseDto responseDto = orderService.addProductToOrder(orderId, requestDto.getProduct_id());

        return new ResponseEntity<>(responseDto.getOrder_id(), HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(
            @PathVariable("orderId") Long orderId
    ) {
        OrderResponseDto responseDto = orderService.findOrderById(orderId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}

