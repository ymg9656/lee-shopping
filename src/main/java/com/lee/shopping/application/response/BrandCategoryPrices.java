package com.lee.shopping.application.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lee.shopping.infrastracture.jackson.PriceSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandCategoryPrices {
    private String brand;
    private List<CategoryPrice> categories;

    @JsonSerialize(using = PriceSerializer.class)
    private Integer totalPrice;
}
