package com.lee.shopping.application.mapper;

import com.lee.shopping.application.response.BrandResponse;
import com.lee.shopping.domain.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BrandResponseMapper {
    BrandResponseMapper INSTANCE = Mappers.getMapper(BrandResponseMapper.class);

    @Mappings({
            @Mapping(target = "brand",source = "id")
    })
    BrandResponse to(Brand p);


}
