package com.lee.shopping.domain.mapper;

import com.lee.shopping.domain.*;
import com.lee.shopping.infrastracture.repository.jpa.entity.ProductRankEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProductRankMapper {
    ProductRankMapper INSTANCE = Mappers.getMapper(ProductRankMapper.class);


    ProductRank fromProduct(ProductRankEntity entity);


    List<ProductRank> fromProducts(List<ProductRankEntity> entities);


    ProductRankEntity toEntity(ProductRank productRank);


    List<ProductRankEntity> toEntities(List<ProductRank> productRanks);



    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "rankKey", source = "rankKey"),

            @Mapping(target = "brandId", source = "product.brandId"),
            @Mapping(target = "categoryId", source = "product.categoryId"),
            @Mapping(target = "productId", source = "product.productId"),
            @Mapping(target = "price", source = "product.price")
    })
    ProductRank to(ProductRankEntity product, RankKey rankKey);


    default List<ProductRank> toList(List<ProductRankEntity> entities, RankKey rankKey){
        if (entities == null) return Collections.emptyList();

        return entities.stream()
                .map(entity -> to(entity, rankKey))
                .collect(Collectors.toList());
    }

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "brandId", source = "product.brand.id"),
            @Mapping(target = "categoryId", source = "product.category.id"),
            @Mapping(target = "productId", source = "product.id"),
            @Mapping(target = "price", source = "product.price")
    })
    ProductRank fromProduct(Product product, RankKey rankKey);


    default List<ProductRank> fromProducts(List<Product> products, RankKey rankKey){
        if (products == null) return Collections.emptyList();

        return products.stream()
                .map(product -> fromProduct(product, rankKey))
                .collect(Collectors.toList());
    }


}