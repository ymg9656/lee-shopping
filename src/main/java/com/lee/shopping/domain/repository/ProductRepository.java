package com.lee.shopping.domain.repository;

import com.lee.shopping.domain.Product;

import java.util.List;

public interface ProductRepository {
    //카테고리별 최저가 상품 조회
    List<Product> findAllLowestPriceForCategory();

    //카테고리별 최고가 상품 조회
    List<Product> findAllHighestPriceForCategory();

    //브랜드별 카테고리별 최저가 상품 조회
    List<Product> findAllLowestPriceForBrandAndCategory();
    Product save(Product product);

}
