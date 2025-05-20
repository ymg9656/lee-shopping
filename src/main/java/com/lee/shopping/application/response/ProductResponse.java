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
public class ProductResponse {
    private Long id;
    private String brand;
    private String category;
    @JsonSerialize(using = PriceSerializer.class)
    private Integer price;
}
