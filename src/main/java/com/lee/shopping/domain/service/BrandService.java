package com.lee.shopping.domain.service;

import com.lee.shopping.domain.Brand;

import java.util.Optional;

public interface BrandService {
    Brand register(Brand brand);

    void remove(String id);

    Brand findById(String id);

}
