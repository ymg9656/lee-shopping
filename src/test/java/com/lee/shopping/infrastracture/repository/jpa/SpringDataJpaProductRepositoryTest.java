package com.lee.shopping.infrastracture.repository.jpa;

import com.lee.shopping.domain.Product;
import com.lee.shopping.infrastracture.repository.jpa.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SpringDataJpaProductRepositoryTest {

    @Mock
    private JpaProductRepository jpaProductRepository;

    @InjectMocks
    private SpringDataJpaProductRepository productRepository;

    List<ProductEntity> productEntities() {
        List<ProductEntity> r = new ArrayList<>();
        r.add(ProductEntity.builder()
                .id(1L)
                .brandId("A")
                .categoryId("상의")
                .price(1000)
                .build());

        return r;
    }

    @Test
    void testFindAllLowestPriceForCategory() {
        // given
        List<ProductEntity> mockProducts = productEntities();

        Mockito.when(jpaProductRepository.findAllLowestPriceForCategory())
                .thenReturn(mockProducts);

        // when
        List<Product> result = productRepository.findAllLowestPriceForCategory();

        // then
        assertThat(result).hasSize(1);
        assertThat(result).extracting("categoryId").contains("상의");
    }
}