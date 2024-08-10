package com.sparta.msa_exam.order.repository;


import com.sparta.msa_exam.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "select o from Order as o left join fetch o.productIds where o.id = :orderId")
    Optional<Order> findByIdProductIds(Long orderId);

}
