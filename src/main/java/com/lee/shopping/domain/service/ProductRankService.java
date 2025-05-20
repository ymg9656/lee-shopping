package com.lee.shopping.domain.service;

import com.lee.shopping.domain.ProductRank;
import com.lee.shopping.domain.Product;
import com.lee.shopping.domain.RankKey;
import org.hibernate.query.SortDirection;

import java.util.List;

public interface ProductRankService {
    void init();

    void update(Product product);

    List<ProductRank> getRanks(RankKey rankKey, int maxRankNo, SortDirection direction);
    List<ProductRank> getRanksByCategoryId(RankKey rankKey, String category, int maxRankNo);
    List<ProductRank> getRanksByCategoryIdDesc(RankKey rankKey, String category, int maxRankNo);


}
