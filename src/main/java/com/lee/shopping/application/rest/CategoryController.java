package com.lee.shopping.application.rest;

import com.lee.shopping.application.mapper.ProductResponseMapper;
import com.lee.shopping.application.response.CategoryLowestHighestResponse;
import com.lee.shopping.application.response.CategoryLowestResponse;
import com.lee.shopping.application.usecase.CategoryUseCase;
import com.lee.shopping.domain.ProductRank;
import com.lee.shopping.domain.RankKey;
import com.lee.shopping.domain.service.ProductRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryUseCase categoryUseCase;



    //카테고리별 최저가격 브랜드와 상품 가격, 총액 조회
    @GetMapping(value = "/lowest")
    ResponseEntity<CategoryLowestResponse> categoriesLowest() {

        return ResponseEntity.ok(categoryUseCase.getCategoriesLowest());

    }

    //카테고리별 최저가격 브랜드와 상품 가격, 총액 조회
    @GetMapping(value = "/{categoryId}/lowest-highest")
    ResponseEntity<CategoryLowestHighestResponse> categoryLowestHighest(@PathVariable String categoryId) {
        return ResponseEntity.ok(categoryUseCase.getCategoriesLowestHighest(categoryId));

    }


}
