package com.lee.shopping.domain.repository;

import com.lee.shopping.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    void testFindAllLowestPriceForCategory() {
        // when
        List<Product> result = productRepository.findAllLowestPriceForCategory();
/*
        Product product = result.stream().filter(e -> e.getCategoryId().equals("스니커즈")).findFirst().get();
        assertThat(product.getBrandId()).isEqualTo("A");
        assertThat(product.getPrice()).isEqualTo(9000);*/
    }
}