package com.lee.shopping.domain.service;

import com.lee.shopping.application.exception.ApplicationException;
import com.lee.shopping.application.mapper.BrandRequestMapper;
import com.lee.shopping.domain.Brand;
import com.lee.shopping.domain.Category;
import com.lee.shopping.domain.Product;
import com.lee.shopping.domain.repository.BrandRepository;
import com.lee.shopping.domain.repository.CategoryRepository;
import com.lee.shopping.domain.repository.ProductRepository;
import com.lee.shopping.infrastracture.cache.CacheNames;
import com.lee.shopping.infrastracture.repository.jpa.entity.BrandEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;


    @CachePut(value = {CacheNames.CAFFEINE_TTL_10M}, key = "'brands:'+#brand.id()")
    @Override
    public Brand register(Brand brand) throws ApplicationException {
        return brandRepository.save(brand);
    }

    @CacheEvict(value = {CacheNames.CAFFEINE_TTL_10M}, key = "'brands:'+#id")
    @Override
    public void remove(String id) {
        brandRepository.deleteById(id);
    }

    @Cacheable(value = {CacheNames.CAFFEINE_TTL_10M}, key = "'brands:'+#id",unless = "#result == null")
    @Override
    public Brand findById(String id) {
        return brandRepository.findById(id).orElse(null);

    }
}
