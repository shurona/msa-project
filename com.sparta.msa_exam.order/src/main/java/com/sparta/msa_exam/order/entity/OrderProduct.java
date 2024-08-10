package com.sparta.msa_exam.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Entity
@Table(name = "order_product")
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "order_product_id")
    private Long id;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    Order order;

    @Column(name = "product_id")
    Long productId;

    public static OrderProduct CreateOrderProduct(Order order, Long productId) {

        OrderProduct orderProduct = new OrderProduct();

        orderProduct.order = order;
        orderProduct.productId = productId;

        return orderProduct;
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "id=" + id +
                ", productId=" + productId +
                '}';
    }
}
