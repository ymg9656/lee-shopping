package com.lee.shopping.domain.repository;

import com.lee.shopping.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    //카테고리별 최저가 상품 조회
    List<Product> findAllLowestPriceForCategoryRankNoLessThanEqual(int rankNo);

    //카테고리별 최고가 상품 조회
    List<Product> findAllHighestPriceForCategoryRankNoLessThanEqual(int rankNo);

    //브랜드별 카테고리별 최저가 상품 조회
    List<Product> findAllLowestPriceForBrandAndCategoryRankNoLessThanEqual(int rankNo);
    Product save(Product product);
    void deleteById(Long productId);
    Optional<Product> findById(Long productId);

    void deleteAllByBrandId(String brandId);
}
