package com.lee.shopping.application.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lee.shopping.infrastracture.jackson.PriceSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCategoryPrice {
    private String category;

    private String price;
}
