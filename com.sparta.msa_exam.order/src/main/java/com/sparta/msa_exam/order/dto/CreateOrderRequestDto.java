package com.sparta.msa_exam.order.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class CreateOrderRequestDto {
    String name;
    List<Long> product_ids;
}
