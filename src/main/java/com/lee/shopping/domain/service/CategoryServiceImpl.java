package com.lee.shopping.domain.service;

import com.lee.shopping.domain.Category;
import com.lee.shopping.domain.mapper.CategoryMapper;
import com.lee.shopping.domain.repository.BrandRepository;
import com.lee.shopping.domain.repository.CategoryRepository;
import com.lee.shopping.infrastracture.cache.CacheNames;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Cacheable(value = {CacheNames.CAFFEINE_TTL_10M}, key = "'categories:'+#id",unless = "#result == null")
    @Override
    public Category findById(String id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Cacheable(value = {CacheNames.CAFFEINE_TTL_10M}, key = "'categories:count'")
    @Override
    public Long count() {
        return categoryRepository.count();
    }
}
