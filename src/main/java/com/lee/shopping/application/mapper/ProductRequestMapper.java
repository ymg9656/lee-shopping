package com.lee.shopping.application.mapper;

import com.lee.shopping.application.request.ProductRequest;
import com.lee.shopping.application.response.BrandPrice;
import com.lee.shopping.application.response.CategoryBrandPrice;
import com.lee.shopping.application.response.CategoryPrice;
import com.lee.shopping.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductRequestMapper {
    ProductRequestMapper INSTANCE = Mappers.getMapper(ProductRequestMapper.class);


}
