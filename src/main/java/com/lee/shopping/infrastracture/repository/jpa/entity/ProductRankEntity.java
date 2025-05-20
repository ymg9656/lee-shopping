package com.lee.shopping.infrastracture.repository.jpa.entity;

import com.lee.shopping.domain.RankKey;
import com.lee.shopping.infrastracture.repository.jpa.entity.id.RankKeyBrandCategoryProductId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@IdClass(RankKeyBrandCategoryProductId.class)
@Table(name = "product_rank")
public class ProductRankEntity {
    @Id
    @Enumerated(EnumType.STRING) // Enum 타입을 문자열로 저장
    @Column(name = "rank_key",nullable = false)
    private RankKey rankKey;

    @Id
    @Column(name = "brand_id",nullable = false)
    private String brandId;

    @Id
    @Column(name = "category_id",nullable = false)
    private String categoryId;

    @Id
    @Column(name = "product_id",nullable = false)
    private Long productId;


    @Transient
    private Integer rankNo;

    @Transient
    private Integer categoryCount;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}