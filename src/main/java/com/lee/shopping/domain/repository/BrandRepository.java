package com.lee.shopping.domain.repository;

import com.lee.shopping.domain.Brand;

import java.util.Optional;

public interface BrandRepository {
    Optional<Brand> findById(String id);

}
