package com.lee.shopping.domain.repository;

import com.lee.shopping.domain.ProductRank;

import java.util.List;

public interface ProductRankRepository {
    List<ProductRank> findAllByRankKeyAndCategoryIdAndRankNoLessThanEqual(String rankKey, String categoryId, Integer rankNo);
    List<ProductRank> findAllByRankKeyAndCategoryIdAndRankNoLessThanEqualRankOrderDesc(String rankKey, String categoryId, Integer rankNo);
    List<ProductRank> findAllByRankKeyAndRankNoLessThanEqual(String rankKey, Integer rankNo);
    List<ProductRank> findAllByRankKeyAndBrandIdAndRankNoLessThanEqual(String rankKey, String brandId, Integer rankNo);
    List<ProductRank> findAllByRankKeyAndBrandIdAndCategoryIdAndRankNoLessThanEqual(String name, String brandId, String categoryId, Integer rankNo);
    List<ProductRank> findAllBrandSetLowestCalculate();
    ProductRank save(ProductRank productRank);

    List<ProductRank> saveAll(List<ProductRank> productRanks);


    void delete(ProductRank productRank);

    void deleteAllByProductId(Long productId);

    void deleteAllByBrandId(String brandId);
}
