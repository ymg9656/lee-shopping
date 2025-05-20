package com.lee.shopping.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandLowestTotalResponse {
    private BrandCategoryPrices lowest;

}
