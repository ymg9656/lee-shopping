package com.lee.shopping.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brand {
    private String id;
    private Integer sortNo;
    private LocalDateTime createdAt;

}
