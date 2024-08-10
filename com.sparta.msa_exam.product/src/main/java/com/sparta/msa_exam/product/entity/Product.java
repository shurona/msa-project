package com.sparta.msa_exam.product.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "product_id")
    private Long id;

    @Column
    private String name;

    @Column(name = "supply_price")
    private int supplyPrice;

    public static Product CreateProduct(String name, int supplyPrice) {
        Product product = new Product();
        product.name = name;
        product.supplyPrice = supplyPrice;
        return product;
    }
}
