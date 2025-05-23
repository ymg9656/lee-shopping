package com.lee.shopping.application.usecase;

import com.lee.shopping.application.exception.ApplicationException;
import com.lee.shopping.application.exception.ExceptionCode;
import com.lee.shopping.application.mapper.ProductResponseMapper;
import com.lee.shopping.application.response.CategoryLowestHighestResponse;
import com.lee.shopping.application.response.CategoryLowestResponse;
import com.lee.shopping.domain.ProductRank;
import com.lee.shopping.domain.RankKey;
import com.lee.shopping.domain.service.CategoryService;
import com.lee.shopping.domain.service.ProductRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryUseCase {

    private final ProductRankService productRankService;
    private final CategoryService categoryService;

    public CategoryLowestResponse getCategoriesLowest() {

        List<ProductRank> ranks = productRankService.getRanks(RankKey.CATEGORY_LOWEST, 1);

        return CategoryLowestResponse.builder()
                .categories(ProductResponseMapper.INSTANCE.toCategoryBrandPrices(ranks))
                .totalPrice(ProductResponseMapper.INSTANCE.calculateTotalPrice(ranks))
                .build();

    }

    public CategoryLowestHighestResponse getCategoriesLowestHighest(String category) {

        Optional.ofNullable(categoryService.findById(category)).orElseThrow(()
                -> new ApplicationException(ExceptionCode.INVALID_REQUEST,"category"));

        List<ProductRank> lowestRanks = productRankService.getRanksByCategoryId(RankKey.CATEGORY_LOWEST, category, 1);

        List<ProductRank> highestRanks = productRankService.getRanksByCategoryIdDesc(RankKey.CATEGORY_HIGHEST, category, 1);

        return CategoryLowestHighestResponse.builder()
                .category(category)
                .lowest(ProductResponseMapper.INSTANCE.toBrandPrices(lowestRanks))
                .highest(ProductResponseMapper.INSTANCE.toBrandPrices(highestRanks))
                .build();
    }
}
