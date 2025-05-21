package com.lee.shopping.domain;

import com.lee.shopping.infrastracture.repository.jpa.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Product {

    private Long id;
    private Category category;
    private Brand brand;
    private Integer price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void updatePrice(Integer price){
        this.price=price;
    }

}
