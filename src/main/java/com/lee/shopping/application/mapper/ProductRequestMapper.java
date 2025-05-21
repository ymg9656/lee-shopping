package com.lee.shopping.application.mapper;

import com.lee.shopping.application.request.ProductRequest;
import com.lee.shopping.application.response.BrandPrice;
import com.lee.shopping.application.response.CategoryBrandPrice;
import com.lee.shopping.application.response.CategoryPrice;
import com.lee.shopping.domain.Brand;
import com.lee.shopping.domain.Category;
import com.lee.shopping.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductRequestMapper {
    ProductRequestMapper INSTANCE = Mappers.getMapper(ProductRequestMapper.class);


    default Product toProduct(Long productId,ProductRequest request){
        return Product.builder()
                .id(productId)
                .brand(Brand.builder().id(request.getBrand()).build())
                .category(Category.builder().id(request.getCategory()).build())
                .price(request.getPrice())
                .build();
    }
    default Product toProduct(ProductRequest request){
        return Product.builder()
                .brand(Brand.builder().id(request.getBrand()).build())
                .category(Category.builder().id(request.getCategory()).build())
                .price(request.getPrice())
                .build();
    }
}
