package com.lee.shopping.domain.repository;

import com.lee.shopping.domain.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandRepository {
    Brand save(Brand brand);
    Optional<Brand> findById(String id);
    List<Brand> findAll();
    void deleteById(String id);
}
