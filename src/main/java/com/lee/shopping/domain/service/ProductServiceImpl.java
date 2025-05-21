package com.lee.shopping.domain.service;

import com.lee.shopping.domain.Brand;
import com.lee.shopping.domain.Category;
import com.lee.shopping.domain.Product;
import com.lee.shopping.domain.repository.BrandRepository;
import com.lee.shopping.domain.repository.CategoryRepository;
import com.lee.shopping.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Override
    public Product register(Product product) throws Exception {
        //1. 카테고리 존재유무
        Optional<Category> category = categoryRepository.findById(product.getCategory().getId());
        if (category.isEmpty()) {
            //존재하지 않는 카테고리 에러
            throw new Exception();
        }
        //2. 브랜드 존재유무
        Optional<Brand> brand = brandRepository.findById(product.getBrand().getId());
        if (brand.isEmpty()) {
            //존재하지 않는 브랜드 에러
            throw new Exception();
        }
        //3. 등록
        return productRepository.save(product);
    }

    @Override
    public Product modify(Product product) throws Exception {

        //1. 카테고리 존재유무
        Optional<Category> category = categoryRepository.findById(product.getCategory().getId());
        if (category.isEmpty()) {
            //존재하지 않는 카테고리 에러
            throw new Exception();
        }
        //2. 브랜드 존재유무
        Optional<Brand> brand = brandRepository.findById(product.getBrand().getId());
        if (brand.isEmpty()) {
            //존재하지 않는 브랜드 에러
            throw new Exception();
        }
        return productRepository.save(product);
    }

    @Override
    public void remove(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Optional<Product> getProductById(Long productId) {

        return productRepository.findById(productId);
    }

    @Transactional
    @Override
    public void removeAllByBrandId(String brandId) {
        productRepository.deleteAllByBrandId(brandId);
    }

}
