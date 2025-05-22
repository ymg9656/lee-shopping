package com.lee.shopping.domain.mapper;

import com.lee.shopping.domain.Brand;
import com.lee.shopping.domain.Category;
import com.lee.shopping.domain.Product;
import com.lee.shopping.domain.ProductRank;
import com.lee.shopping.infrastracture.repository.jpa.entity.CategoryEntity;
import com.lee.shopping.infrastracture.repository.jpa.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category to(CategoryEntity entity);

    List<Category> toList(List<CategoryEntity> entities);
}