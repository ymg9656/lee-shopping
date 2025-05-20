package com.lee.shopping.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class ProductRank {
    private RankKey rankKey;
    private String brandId;
    private String categoryId;
    private Long productId;
    private Integer rankNo;
    private Integer price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}