package com.lee.shopping.domain.service;

import com.lee.shopping.domain.Category;

import java.util.Optional;

public interface CategoryService {
    Category findById(String id);
    Long count();

}
