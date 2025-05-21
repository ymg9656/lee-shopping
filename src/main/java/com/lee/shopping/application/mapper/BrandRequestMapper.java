package com.lee.shopping.application.mapper;

import com.lee.shopping.application.request.BrandRequest;
import com.lee.shopping.application.request.ProductRequest;
import com.lee.shopping.domain.Brand;
import com.lee.shopping.domain.Category;
import com.lee.shopping.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BrandRequestMapper {
    BrandRequestMapper INSTANCE = Mappers.getMapper(BrandRequestMapper.class);

    @Mappings({
            @Mapping(target = "id",source = "brand")
    })
    Brand toBrand(BrandRequest request);
}
