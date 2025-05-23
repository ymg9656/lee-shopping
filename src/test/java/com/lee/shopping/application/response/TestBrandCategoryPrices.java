package com.lee.shopping.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestBrandCategoryPrices {
    private String brand;
    private List<TestCategoryPrice> categories;
    private String totalPrice;
}
