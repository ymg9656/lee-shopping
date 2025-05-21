package com.lee.shopping.domain.service;

import com.lee.shopping.domain.Product;

import java.util.Optional;

public interface ProductService {
    Product register(Product product) throws Exception;

    Product modify(Product product) throws Exception;

    void remove(Long id);

    Optional<Product> getProductById(Long productId);

    void removeAllByBrandId(String brandId);
}
