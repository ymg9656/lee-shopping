package com.lee.shopping.domain.repository;

import com.lee.shopping.domain.Brand;

import java.util.Optional;

public interface BrandRepository {
    Brand save(Brand brand);
    Optional<Brand> findById(String id);
    void deleteById(String id);
}
