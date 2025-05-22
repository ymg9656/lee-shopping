package com.lee.shopping.infrastracture.repository.jpa;

import com.lee.shopping.domain.Product;
import com.lee.shopping.domain.mapper.ProductMapper;
import com.lee.shopping.domain.repository.ProductRepository;
import com.lee.shopping.infrastracture.repository.jpa.entity.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Component
public class SpringDataJpaProductRepository implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;

    @Autowired
    public SpringDataJpaProductRepository(final JpaProductRepository jpaProductRepository) {
        this.jpaProductRepository = jpaProductRepository;
    }

    @Override
    public List<Product> findAllLowestPriceForCategoryRankNoLessThanEqual(int rankno) {
        return ProductMapper.INSTANCE.fromEntities(jpaProductRepository.findAllLowestPriceForCategoryRankNoLessThanEqual(rankno));
    }

    @Override
    public List<Product> findAllHighestPriceForCategoryRankNoLessThanEqual(int rankno) {
        return ProductMapper.INSTANCE.fromEntities(jpaProductRepository.findAllHighestPriceForCategoryRankNoLessThanEqual(rankno));
    }

    @Override
    public List<Product> findAllLowestPriceForBrandAndCategoryRankNoLessThanEqual(int rankno) {
        return ProductMapper.INSTANCE.fromEntities(jpaProductRepository.findAllLowestPriceForBrandAndCategoryRankNoLessThanEqual(rankno));
    }

    @Override
    public Product save(Product product) {
        return ProductMapper.INSTANCE.fromEntity(jpaProductRepository.save(ProductMapper.INSTANCE.to(product)));
    }

    @Override
    public List<Product> saveAll(List<Product> products) {
        //테스트 용으로 벌크 인서트
        //실서비스에서 벌크 인서트가 필요한 경우에는 jdbcTemplate 사용하는게 가장 성능이 좋음.
        return ProductMapper.INSTANCE.fromEntities(jpaProductRepository.saveAll(ProductMapper.INSTANCE.toList(products)));
    }

    @Override
    public void deleteById(Long productId) {
        jpaProductRepository.deleteById(productId);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return jpaProductRepository.findById(productId).map(ProductMapper.INSTANCE::fromEntity);
    }

    @Transactional
    @Override
    public void deleteAllByBrandId(String brandId) {
        jpaProductRepository.deleteAllByBrandId(brandId);
    }

    @Override
    public List<Product> findAll() {
        return ProductMapper.INSTANCE.fromEntities(jpaProductRepository.findAll());
    }
}
