package com.lee.shopping.domain.service;

import com.lee.shopping.application.exception.ApplicationException;
import com.lee.shopping.domain.Product;

import java.util.Optional;

public interface ProductService {
    Product register(Product product) throws ApplicationException;

    Product modify(Product product) throws ApplicationException;

    void remove(Long id);

    Optional<Product> getProductById(Long productId);

    void removeAllByBrandId(String brandId);
}
