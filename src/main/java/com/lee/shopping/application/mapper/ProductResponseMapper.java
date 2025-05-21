package com.lee.shopping.application.mapper;

import com.lee.shopping.application.response.BrandPrice;
import com.lee.shopping.application.response.CategoryBrandPrice;
import com.lee.shopping.application.response.CategoryPrice;
import com.lee.shopping.application.response.ProductResponse;
import com.lee.shopping.domain.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ProductResponseMapper {
    ProductResponseMapper INSTANCE = Mappers.getMapper(ProductResponseMapper.class);



    @Mappings({
            @Mapping(target = "category", source = "category.id"),
            @Mapping(target = "brand", source = "brand.id")
    })
    ProductResponse to(Product p);







    // 4. 총합 계산용 메서드
    default Integer calculateTotalPrice(List<ProductRank> product) {
        return product.stream()
                .mapToInt(p -> p.getPrice() == null ? 0 : p.getPrice())
                .sum();
    }

    // 1. Product → CategoryBrandPrice 매핑
    @Mappings({
            @Mapping(target = "category", source = "categoryId")
            , @Mapping(target = "brand", source = "brandId")
    })
    CategoryBrandPrice toCategoryBrandPrice(ProductRank product);

    List<CategoryBrandPrice> toCategoryBrandPrices(List<ProductRank> products);


    // 2. Product → BrandPrice 매핑
    @Mappings({
            @Mapping(target = "brand", source = "brandId")
    })
    BrandPrice toBrandPrice(ProductRank product);

    List<BrandPrice> toBrandPrices(List<ProductRank> products);


    // 3. Product → CategoryPrice 매핑
    @Mappings({
            @Mapping(target = "category", source = "categoryId")
    })
    CategoryPrice toCategoryPrice(ProductRank product);

    List<CategoryPrice> toCategoryPrices(List<ProductRank> products);

}
