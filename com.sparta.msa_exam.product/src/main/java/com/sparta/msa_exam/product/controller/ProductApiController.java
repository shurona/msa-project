package com.sparta.msa_exam.product.controller;


import com.sparta.msa_exam.product.dto.CreateProductRequestDto;
import com.sparta.msa_exam.product.dto.ProductResponseDto;
import com.sparta.msa_exam.product.entity.Product;
import com.sparta.msa_exam.product.service.ProductService;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("products")
public class ProductApiController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Long> createProduct(@RequestBody CreateProductRequestDto requestDto) {
        Long productId = productService.createProduct(requestDto.getName(), requestDto.getSupply_price());
        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> productList(
            @RequestHeader(value = "userId", required = false) String userId
    ) {
        List<ProductResponseDto> output = new ArrayList<>();
        for (Product product : productService.findProductList()) {
            output.add(
                    new ProductResponseDto(product.getId(), product.getName(), product.getSupplyPrice()));
        }

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Boolean getProductById(@PathVariable("id") Long productId) {
        Product product = productService.findByProductIdForOrder(productId);
        return product != null;
    }
}

