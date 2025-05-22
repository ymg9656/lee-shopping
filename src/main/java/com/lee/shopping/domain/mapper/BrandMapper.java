package com.lee.shopping.domain.mapper;

import com.lee.shopping.domain.Brand;
import com.lee.shopping.domain.Category;
import com.lee.shopping.infrastracture.repository.jpa.entity.BrandEntity;
import com.lee.shopping.infrastracture.repository.jpa.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BrandMapper {
    BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);

    Brand to(BrandEntity entity);
    List<Brand> toList(List<BrandEntity> entity);
    BrandEntity fromBrand(Brand brand);
}