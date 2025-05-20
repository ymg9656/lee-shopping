package com.lee.shopping.application.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    @NotBlank
    @Size(max = 10)
    private String brand;

    @NotBlank
    @Size(max = 10)
    private String category;

    @Min(value = 1)
    @Max(value = 100000000)
    private Integer price;
}
