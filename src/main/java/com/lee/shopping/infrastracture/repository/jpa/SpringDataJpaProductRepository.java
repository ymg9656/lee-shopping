package com.lee.shopping.infrastracture.repository.jpa;

import com.lee.shopping.domain.Product;
import com.lee.shopping.domain.mapper.ProductMapper;
import com.lee.shopping.domain.repository.ProductRepository;
import com.lee.shopping.infrastracture.repository.jpa.entity.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class SpringDataJpaProductRepository implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;

    @Autowired
    public SpringDataJpaProductRepository(final JpaProductRepository jpaProductRepository) {
        this.jpaProductRepository = jpaProductRepository;
    }

    @Override
    public List<Product> findAllLowestPriceForCategory() {
        return ProductMapper.INSTANCE.fromEntities(jpaProductRepository.findAllLowestPriceForCategory());
    }

    @Override
    public List<Product> findAllHighestPriceForCategory() {
        return ProductMapper.INSTANCE.fromEntities(jpaProductRepository.findAllHighestPriceForCategory());
    }

    @Override
    public List<Product> findAllLowestPriceForBrandAndCategory() {
        return ProductMapper.INSTANCE.fromEntities(jpaProductRepository.findAllLowestPriceForBrandAndCategory());
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = jpaProductRepository.save(ProductEntity.builder().build());
        return null;
    }
}
