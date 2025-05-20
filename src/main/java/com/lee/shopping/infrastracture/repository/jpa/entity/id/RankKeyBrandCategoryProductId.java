package com.lee.shopping.infrastracture.repository.jpa.entity.id;

import com.lee.shopping.domain.RankKey;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RankKeyBrandCategoryProductId implements Serializable{
    private RankKey rankKey;
    private String brandId;
    private String categoryId;
    private Long productId;

}

