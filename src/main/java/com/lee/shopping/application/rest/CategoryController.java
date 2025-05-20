package com.lee.shopping.application.rest;

import com.lee.shopping.application.mapper.ProductResponseMapper;
import com.lee.shopping.application.response.CategoryLowestResponse;
import com.lee.shopping.application.response.CategoryLowestHighestResponse;
import com.lee.shopping.domain.ProductRank;
import com.lee.shopping.domain.Product;
import com.lee.shopping.domain.RankKey;
import com.lee.shopping.domain.service.ProductRankService;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.SortDirection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final ProductRankService productRankService;


    //카테고리별 최저가격 브랜드와 상품 가격, 총액 조회
    @GetMapping(value = "/lowest")
    ResponseEntity<CategoryLowestResponse> categoriesLowest() {

        List<ProductRank> ranks = productRankService.getRanks(RankKey.CATEGORY_BRAND_LOWEST,1, SortDirection.ASCENDING);

        return ResponseEntity.ok(CategoryLowestResponse.builder()
                .categories(ProductResponseMapper.INSTANCE.toCategoryBrandPrices(ranks))
                .totalPrice(ProductResponseMapper.INSTANCE.calculateTotalPrice(ranks))
                .build());


    }

    //카테고리별 최저가격 브랜드와 상품 가격, 총액 조회
    @GetMapping(value = "/{category}/lowest-highest")
    ResponseEntity<CategoryLowestHighestResponse> categoryLowestHighest(@PathVariable String category) {
        List<ProductRank> lowestRanks = productRankService.getRanksByCategoryId(RankKey.CATEGORY_BRAND_LOWEST,category,1);



        List<ProductRank> highestRanks = productRankService.getRanksByCategoryIdDesc(RankKey.CATEGORY_BRAND_HIGHEST,category,1);



        return ResponseEntity.ok(CategoryLowestHighestResponse.builder()
                .category(category)
                .lowest(ProductResponseMapper.INSTANCE.toBrandPrices(lowestRanks))
                .highest(ProductResponseMapper.INSTANCE.toBrandPrices(highestRanks))
                .build());
    }


}
