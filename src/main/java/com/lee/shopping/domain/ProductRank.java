package com.lee.shopping.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuperBuilder
public class ProductRank {

    @EqualsAndHashCode.Include
    private RankKey rankKey;

    @EqualsAndHashCode.Include
    private String brandId;

    @EqualsAndHashCode.Include
    private String categoryId;

    @EqualsAndHashCode.Include
    private Long productId;
    private Integer rankNo;
    private Integer price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}