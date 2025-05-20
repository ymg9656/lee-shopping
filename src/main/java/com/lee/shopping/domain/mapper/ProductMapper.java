package com.lee.shopping.domain.mapper;

import com.lee.shopping.domain.Brand;
import com.lee.shopping.domain.Category;
import com.lee.shopping.domain.Product;
import com.lee.shopping.domain.ProductRank;
import com.lee.shopping.infrastracture.repository.jpa.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);


    default Product fromEntity(ProductEntity entity){
        return Product.builder()
                .id(entity.getId())
                .price(entity.getPrice())
                .category(Category.builder().id(entity.getCategoryId()).build())
                .brand(Brand.builder().id(entity.getBrandId()).build())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    List<Product> fromEntities(List<ProductEntity> entities);



    default Product fromRank(ProductRank rank){
        return Product.builder()
                .id(rank.getProductId())
                .price(rank.getPrice())
                .category(Category.builder().id(rank.getCategoryId()).build())
                .brand(Brand.builder().id(rank.getBrandId()).build())
                .createdAt(rank.getCreatedAt())
                .updatedAt(rank.getUpdatedAt())
                .build();
    }

    List<Product> fromRanks(List<ProductRank> ranks);

}