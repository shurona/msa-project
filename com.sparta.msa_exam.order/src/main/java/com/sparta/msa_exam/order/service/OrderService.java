package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.entity.OrderProduct;
import com.sparta.msa_exam.order.feignClient.ProductClient;
import com.sparta.msa_exam.order.repository.OrderProductRepository;
import com.sparta.msa_exam.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductClient productClient;

    private void checkExistProduct(List<Long> productIds) {
        boolean checkExist = true;
        for (Long productId : productIds) {
            Boolean isExist = productClient.getProduct(String.valueOf(productId));
            checkExist = checkExist && isExist;
        }
        if (!checkExist) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "없는 상품 입니다.");
        }
    }


    @Transactional
    public Long createOrders(String name, List<Long> productIdList) {

        checkExistProduct(productIdList);
        Order order = Order.CreateOrder(name);
        List<OrderProduct> orderProductList = new ArrayList<>();
        for (Long productId : productIdList) {
            orderProductList.add(OrderProduct.CreateOrderProduct(order, productId));
        }
        orderRepository.save(order);
        orderProductRepository.saveAll(orderProductList);

        return order.getId();
    }

    @CachePut(cacheNames = "orderCache", key = "args[0]")
    @Transactional
    public OrderResponseDto addProductToOrder(Long orderId, Long productId) {
        ArrayList<Long> productIds = new ArrayList<>();
        productIds.add(productId);
        checkExistProduct(productIds);

        // 주문이 존재하는 지 확인
        Order order = orderRepository.findById(orderId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.BAD_REQUEST ,"주문이 존재하지 않습니다."));

        Optional<OrderProduct> checkOrderProduct = orderProductRepository.findByOrderAndProductId(order, productId);
        if (checkOrderProduct.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품이 이미 추가되어 있습니다.");
        }

        // 상품 추가
        OrderProduct newOrderProduct = OrderProduct.CreateOrderProduct(order, productId);
        orderProductRepository.save(newOrderProduct);

        for (OrderProduct orderProduct : order.getProductIds()) {
            productIds.add(orderProduct.getId());
        }

        return new OrderResponseDto(order.getId(), productIds);
    }

    @Cacheable(cacheNames = "orderCache", key = "args[0]")
    public OrderResponseDto findOrderById(Long orderId) {
        Order order = orderRepository.findByIdProductIds(orderId).orElseThrow(
                () -> {
                    log.error("해당 주문이 존재하지 않습니다.");
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문이 존재하지 않습니다.");
                });

        List<Long> productIds = new ArrayList<>();
        for (OrderProduct orderProduct : order.getProductIds()) {
            productIds.add(orderProduct.getProductId());
        }
        return new OrderResponseDto(order.getId(), productIds);
    }


}

