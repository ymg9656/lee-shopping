package com.lee.shopping.domain.service;

import com.lee.shopping.application.mapper.BrandRequestMapper;
import com.lee.shopping.domain.Brand;
import com.lee.shopping.domain.Category;
import com.lee.shopping.domain.Product;
import com.lee.shopping.domain.repository.BrandRepository;
import com.lee.shopping.domain.repository.CategoryRepository;
import com.lee.shopping.domain.repository.ProductRepository;
import com.lee.shopping.infrastracture.repository.jpa.entity.BrandEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;


    @Override
    public Brand register(Brand brand) throws Exception {
        Optional<Brand> brandOptional = brandRepository.findById(brand.getId());
        if(brandOptional.isPresent()){
            throw new Exception();
        }
        return brandRepository.save(brand);
    }

    @Override
    public void remove(String id) {
        brandRepository.deleteById(id);
    }
}
