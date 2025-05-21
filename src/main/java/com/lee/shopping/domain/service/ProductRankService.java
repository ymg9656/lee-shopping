package com.lee.shopping.domain.service;

import com.lee.shopping.domain.Product;
import com.lee.shopping.domain.ProductRank;
import com.lee.shopping.domain.RankKey;

import java.util.List;

public interface ProductRankService {
    void init();

    void update(Product product);
    void updateBrandSetLowestRanks(String brand);
    List<ProductRank> getRanks(RankKey rankKey, int maxRankNo);

    List<ProductRank> getRanksByBrandId(RankKey rankKey, String brandId, int maxRankNo);

    List<ProductRank> getRanksByBrandIdAndCategoryId(RankKey rankKey, String brandId, String categoryId, int maxRankNo);

    List<ProductRank> getRanksByCategoryId(RankKey rankKey, String category, int maxRankNo);

    List<ProductRank> getRanksByCategoryIdDesc(RankKey rankKey, String category, int maxRankNo);

    void deleteAllByProductId(Long productId);

    void removeAllByBrandId(String brandId);
}
