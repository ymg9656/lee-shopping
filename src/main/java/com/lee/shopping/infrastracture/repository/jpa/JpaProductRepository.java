package com.lee.shopping.infrastracture.repository.jpa;

import com.lee.shopping.domain.Product;
import com.lee.shopping.infrastracture.repository.jpa.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {
    //카테고리별 최저가 상품 조회
    @Query(value = """
                SELECT
                 p.id
                , p.brand_id as brandId
                , p.category_id  as categoryId
                , p.price
                , p.created_at as createdAt
                , p.updated_at as updatedAt
                FROM (
                    SELECT *, RANK() OVER (PARTITION BY category_id ORDER BY price ASC, id ASC) AS r
                    FROM product
                ) p
                WHERE p.r <= :rankNo
            """, nativeQuery = true)
    List<ProductEntity> findAllLowestPriceForCategoryRankNoLessThanEqual(@Param("rankNo")int rankNo);


    //카테고리별 최고가 상품 조회
    @Query(value = """
                SELECT
                 p.id
                , p.brand_id as brandId
                , p.category_id  as categoryId
                , p.price
                , p.created_at as createdAt
                , p.updated_at as updatedAt
                FROM (
                    SELECT *, RANK() OVER (PARTITION BY category_id ORDER BY price DESC, id ASC) AS r
                    FROM product
                ) p
                WHERE p.r <= :rankNo
            """, nativeQuery = true)
    List<ProductEntity> findAllHighestPriceForCategoryRankNoLessThanEqual(@Param("rankNo")int rankNo);


    //브랜드별 카테고리별 최저가 상품 조회
    @Query(value = """
                SELECT
                 p.id
                , p.brand_id AS brandId
                , p.category_id AS categoryId
                , p.price
                , p.created_at AS createdAt
                , p.updated_at AS updatedAt
                 FROM (
                     SELECT *, RANK() OVER (PARTITION BY brand_id, category_id ORDER BY price ASC, id ASC) AS r
                 FROM product
             ) p
                 WHERE p.r <= :rankNo
            """, nativeQuery = true)
    List<ProductEntity> findAllLowestPriceForBrandAndCategoryRankNoLessThanEqual(@Param("rankNo") int rankNo);


    @Modifying
    @Query(value = """
                DELETE FROM product WHERE brand_id = :brandId
            """, nativeQuery = true)
    void deleteAllByBrandId(@Param("brandId") String brandId);
}
