package com.lee.shopping.domain.service;

import com.lee.shopping.domain.Product;

import java.util.List;

public interface ProductService {
    Product register(Product product) throws Exception;

    Product modify(Product product);

    void remove(Long id);

    List<Product> findAllLowestPriceForCategory();

}
