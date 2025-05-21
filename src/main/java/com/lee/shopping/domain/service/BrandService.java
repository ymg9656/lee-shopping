package com.lee.shopping.domain.service;

import com.lee.shopping.domain.Brand;
import com.lee.shopping.domain.Product;

import java.util.Optional;

public interface BrandService {
    Brand register(Brand brand) throws Exception;

    void remove(String id);

}
