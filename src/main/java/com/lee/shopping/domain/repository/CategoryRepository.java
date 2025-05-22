package com.lee.shopping.domain.repository;

import com.lee.shopping.domain.Category;
import com.lee.shopping.domain.Product;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findById(String id);
    List<Category> findAll();
    Long count();
}
