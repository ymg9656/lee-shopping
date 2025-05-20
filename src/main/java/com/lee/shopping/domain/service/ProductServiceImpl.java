package com.lee.shopping.domain.service;

import com.lee.shopping.domain.Brand;
import com.lee.shopping.domain.Category;
import com.lee.shopping.domain.Product;
import com.lee.shopping.domain.repository.BrandRepository;
import com.lee.shopping.domain.repository.CategoryRepository;
import com.lee.shopping.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;


    @Override
    public Product register(Product product) throws Exception {
        //1. 카테고리 존재유무
        Optional<Category> category = categoryRepository.findById(product.getCategory().getId());
        if(!category.isPresent()){
            //존재하지 않는 카테고리 에러
            throw new Exception();
        }
        //2. 브랜드 존재유무
        Optional<Brand> brand = brandRepository.findById(product.getBrand().getId());
        if(!brand.isPresent()){
            //존재하지 않는 브랜드 에러
            throw new Exception();
        }
        //3. 등록
        productRepository.save(product);


        return null;
    }

    @Override
    public Product modify(Product product) {
        //1. 카테고리 존재유무
        //2. 브랜드 존재유무
        //3. 수정

        return null;
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public List<Product> findAllLowestPriceForCategory() {
        return productRepository.findAllLowestPriceForCategory();

    }
}
