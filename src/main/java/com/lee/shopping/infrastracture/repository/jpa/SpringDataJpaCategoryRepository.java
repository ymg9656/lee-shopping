package com.lee.shopping.infrastracture.repository.jpa;

import com.lee.shopping.domain.Category;
import com.lee.shopping.domain.mapper.CategoryMapper;
import com.lee.shopping.domain.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class SpringDataJpaCategoryRepository implements CategoryRepository {

    private final JpaCategoryRepository jpaCategoryRepository;

    @Autowired
    public SpringDataJpaCategoryRepository(final JpaCategoryRepository jpaCategoryRepository) {
        this.jpaCategoryRepository = jpaCategoryRepository;
    }

    @Override
    public Optional<Category> findById(String id) {

        return jpaCategoryRepository.findById(id).map(CategoryMapper.INSTANCE::to);

    }

    @Override
    public List<Category> findAll() {
        return CategoryMapper.INSTANCE.toList(jpaCategoryRepository.findAll());
    }

    @Override
    public Long count() {
        return jpaCategoryRepository.count();
    }
}
