package com.lee.shopping.infrastracture.repository.jpa;

import com.lee.shopping.infrastracture.repository.jpa.entity.ProductRankEntity;
import com.lee.shopping.infrastracture.repository.jpa.entity.id.RankKeyBrandCategoryProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaProductRankRepository extends JpaRepository<ProductRankEntity, RankKeyBrandCategoryProductId>{
    //RANK 굳이 할 필요가 없을거 같은데?
    @Query(value = """
                SELECT
                 p.rank_key as rankKey
                , p.brand_id as brandId
                , p.category_id  as categoryId
                , p.product_id as productId
                , p.r as rankNo
                , p.price
                , p.created_at as createdAt
                , p.updated_at as updatedAt
                FROM (
                    SELECT *, RANK() OVER (PARTITION BY category_id ORDER BY price asc, product_id ASC) AS r
                    FROM product_rank
                    WHERE rank_key = :rankKey
                ) p
                LEFT JOIN category c
                ON p.category_id = c.id
                WHERE p.r <= :rankNo
                ORDER BY c.sort_no, p.r
            """, nativeQuery = true)
    List<ProductRankEntity> findAllByRankKeyAndRankNoLessThanEqual(@Param("rankKey") String rankKey, @Param("rankNo") int rankNo);


    @Query(value = """
                SELECT
                p.rank_key as rankKey
                , p.brand_id as brandId
                , p.category_id  as categoryId
                , p.product_id as productId
                , p.r as rankNo
                , p.price
                , p.created_at as createdAt
                , p.updated_at as updatedAt
                FROM (
                    SELECT *, RANK() OVER (PARTITION BY category_id ORDER BY price asc, product_id ASC) AS r
                    FROM product_rank
                    WHERE rank_key=:rankKey AND category_id=:categoryId
                ) p
                WHERE p.r <= :rankNo
            """, nativeQuery = true)
    List<ProductRankEntity> findAllByRankKeyAndCategoryIdAndRankNoLessThanEqual(@Param("rankKey") String rankKey, @Param("categoryId") String categoryId, @Param("rankNo") int rankNo);

    @Query(value = """
                SELECT
                p.rank_key as rankKey
                , p.brand_id as brandId
                , p.category_id  as categoryId
                , p.product_id as productId
                , p.r as rankNo
                , p.price
                , p.created_at as createdAt
                , p.updated_at as updatedAt
                FROM (
                    SELECT *, RANK() OVER (PARTITION BY category_id ORDER BY price desc, product_id ASC) AS r
                    FROM product_rank
                    WHERE rank_key=:rankKey AND category_id=:categoryId
                ) p
                WHERE p.r <= :rankNo
            """, nativeQuery = true)
    List<ProductRankEntity> findAllByRankKeyAndCategoryIdAndRankNoLessThanEqualRankOrderDesc(@Param("rankKey") String rankKey, @Param("categoryId") String categoryId, @Param("rankNo") int rankNo);


    //브랜드별 카테고리 랭킹조회
    @Query(value = """
                SELECT
                     min(p.rank_key) as rank_key,
                     brand_id,
                     '0' AS category_id,
                     0 AS product_id,
                     SUM(price) AS price,
                     COUNT(*) AS categoryCount,
                     min(p.created_at) as created_at,
                     min(p.updated_at) as updated_at
                 FROM (
                     SELECT
                         *, RANK() OVER (PARTITION BY brand_id, category_id ORDER BY price ASC, product_id ASC) AS r
                     FROM product_rank
                     WHERE rank_key = :rankKey
                 ) p
                 WHERE p.r = 1
                 GROUP BY p.brand_id
                 HAVING COUNT(*) = (SELECT COUNT(*) FROM category)
                 ORDER BY SUM(price)
                 LIMIT :limit
            """, nativeQuery = true)
    List<ProductRankEntity> findAllBrandSetLowestCalculate(@Param("rankKey") String rankKey, @Param("limit") int limit);




    @Query(value = """
                SELECT
                 p.rank_key as rankKey
                , p.brand_id as brandId
                , p.category_id  as categoryId
                , p.product_id as productId
                , p.r as rankNo
                , p.price
                , p.created_at as createdAt
                , p.updated_at as updatedAt
                FROM (
                    SELECT *, RANK() OVER (PARTITION BY category_id ORDER BY price asc, product_id ASC) AS r
                    FROM product_rank
                    WHERE rank_key = :rankKey AND brand_id = :brandId
                ) p
                LEFT JOIN category c
                ON p.category_id = c.id
                WHERE p.r <= :rankNo
                ORDER BY c.sort_no, p.r
            """, nativeQuery = true)
    List<ProductRankEntity> findAllByRankKeyAndBrandIdAndRankNoLessThanEqual(@Param("rankKey") String rankKey, @Param("brandId") String brandId, @Param("rankNo") Integer rankNo);



    @Query(value = """
                SELECT
                 p.rank_key as rankKey
                , p.brand_id as brandId
                , p.category_id  as categoryId
                , p.product_id as productId
                , p.r as rankNo
                , p.price
                , p.created_at as createdAt
                , p.updated_at as updatedAt
                FROM (
                    SELECT *, RANK() OVER (PARTITION BY category_id ORDER BY price asc, product_id ASC) AS r
                    FROM product_rank
                    WHERE rank_key = :rankKey AND brand_id = :brandId AND category_id = :categoryId
                ) p
                LEFT JOIN category c
                ON p.category_id = c.id
                WHERE p.r <= :rankNo
                ORDER BY c.sort_no, p.r
            """, nativeQuery = true)
    List<ProductRankEntity> findAllByRankKeyAndBrandIdAndCategoryIdRankNoLessThanEqual(@Param("rankKey") String rankKey, @Param("brandId") String brandId, @Param("categoryId") String categoryId,@Param("rankNo") Integer rankNo);



    @Modifying
    @Query(value = """
                DELETE FROM product_rank WHERE product_id =:productId
            """, nativeQuery = true)
    void deleteAllByProductId(@Param("productId") Long productId);


    @Modifying
    @Query(value = """
                DELETE FROM product_rank WHERE brand_id =:brandId
            """, nativeQuery = true)
    void deleteAllByBrandId(@Param("brandId") String brandId);


    List<ProductRankEntity> findAllByProductId(Long productId);
}
