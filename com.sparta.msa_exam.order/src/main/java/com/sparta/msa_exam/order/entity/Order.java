package com.sparta.msa_exam.order.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "order_id")
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "order")
    List<OrderProduct> productIds = new ArrayList<>();

    public static Order CreateOrder(String name) {
        Order order = new Order();
        order.name = name;
        return order;
    }
}

