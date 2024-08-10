package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.entity.Product;
import com.sparta.msa_exam.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@AllArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Transactional
    public Long createProduct(String name, int supplyPrice) {

        Product product = productRepository.save(Product.CreateProduct(name, supplyPrice));
        return product.getId();
    }

    public List<Product> findProductList() {
        return productRepository.findAll();
    }

    public Product findByProductIdForOrder(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

}

