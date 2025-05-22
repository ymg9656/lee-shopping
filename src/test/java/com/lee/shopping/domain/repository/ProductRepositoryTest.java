package com.lee.shopping.domain.repository;

import com.lee.shopping.domain.*;
import com.lee.shopping.domain.mapper.ProductRankMapper;
import com.lee.shopping.infrastracture.repository.jpa.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;



    private Product createProduct(String brandId, String categoryId, int price) {
        return Product.builder()
                .brand(Brand.builder().id(brandId).build())
                .category(Category.builder().id(categoryId).build())
                .price(price)
                .build();
    }


    //카테고리별 브랜드 최저가 테스트
    @Test
    void testFindAllLowestPriceForCategoryRankNoLessThanEqual() {
        // given - A브랜드로 모든 카테고리 상품 최저값으로 셋팅
        String brandId="A";
        List<Category> categories = categoryRepository.findAll();
        List<Product> r= new ArrayList<>();
        for(Category category:categories){
            int price = 1;
            r.add(createProduct(brandId,category.getId(),price));
        }
        productRepository.saveAll(r);

        // when - 카테고리별 브랜드 조회
        List<Product> result = productRepository.findAllLowestPriceForCategoryRankNoLessThanEqual(1);

        // then - 모든 결과의 brandId는 "A"여야 한다
        assertThat(result)
                .isNotEmpty()
                .allMatch(product -> "A".equals(product.getBrand().getId()));


    }

    //카테고리별 브랜드 최고가 테스트
    @Test
    void testFindAllHighestPriceForCategoryRankNoLessThanEqual() {
        // given - A브랜드로 모든 카테고리 상품 최고값으로 셋팅
        String brandId="A";
        List<Category> categories = categoryRepository.findAll();
        List<Product> r= new ArrayList<>();
        for(Category category:categories){
            int price = 99999999;
            r.add(createProduct(brandId,category.getId(),price));
        }
        productRepository.saveAll(r);

        // when - 카테고리별 브랜드 조회
        List<Product> result = productRepository.findAllHighestPriceForCategoryRankNoLessThanEqual(1);

        // then - 모든 결과의 brandId는 "A"여야 한다
        assertThat(result)
                .isNotEmpty()
                .allMatch(product -> "A".equals(product.getBrand().getId()));


    }

    //브랜드별 카테고리 최저가 테스트
    @Test
    void testFindAllLowestPriceForBrandAndCategoryRankNoLessThanEqual() {
        // given - A브랜드로 모든 카테고리 상품 최고값으로 셋팅
        String brandId="A";
        List<Category> categories = categoryRepository.findAll();
        List<Product> r= new ArrayList<>();
        for(Category category:categories){
            int price = 1;
            r.add(createProduct(brandId,category.getId(),price));
        }
        productRepository.saveAll(r);

        // when - 브랜드별 카테고리 최저가
        List<Product> result = productRepository.findAllLowestPriceForBrandAndCategoryRankNoLessThanEqual(1);
        List<Product> brandAresult = result.stream()
                .filter(e -> e.getBrand().getId().equals(brandId))
                .toList();

        // then - A 브랜드의 모든 결과의 price는 1
        assertThat(brandAresult)
                .isNotEmpty()
                .allMatch(product -> 1 == product.getPrice());

    }




}