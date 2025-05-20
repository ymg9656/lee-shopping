package com.lee.shopping.infrastracture.repository.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "brand")
public class BrandEntity {


    @Id
    @Column(length = 10)
    private String id;

    @Column(name = "sort_no")
    private Integer sortNo;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    @PrePersist
    protected void onCreate() {
        this.createdAt =LocalDateTime.now();
    }

}