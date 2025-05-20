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
public class CategoryLowestHighestResponse {
    private String category;
    private List<BrandPrice> lowest;
    private List<BrandPrice> highest;

}
