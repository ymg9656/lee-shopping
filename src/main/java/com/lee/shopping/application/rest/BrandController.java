package com.lee.shopping.application.rest;

import com.lee.shopping.application.mapper.ProductResponseMapper;
import com.lee.shopping.application.request.BrandRequest;
import com.lee.shopping.application.response.BrandCategoryPrices;
import com.lee.shopping.application.response.BrandLowestTotalResponse;
import com.lee.shopping.application.response.BrandResponse;
import com.lee.shopping.domain.Product;
import com.lee.shopping.domain.service.ProductRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/brands")
public class BrandController {


    private final ProductRankService productRankService;

    //단일 브랜드로 모든 카테고리 상품을 최저가격으로 구매할 때의 정보 조회
    @GetMapping(value = "/categories/lowest")
    ResponseEntity<BrandLowestTotalResponse> getBrandCategoriesLowest(@PathVariable String brand) {

        return null;

    }


    //브랜드 추가
    @PostMapping
    ResponseEntity<BrandResponse> register(@RequestBody BrandRequest request) {
        return ResponseEntity.ok().build();
    }
    //브랜드 수정
    @PutMapping(value = "/{brand}")
    ResponseEntity<BrandResponse> modify(@PathVariable String brandId,@RequestBody BrandRequest request) {
        return ResponseEntity.ok().build();
    }
    //브랜드 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    @DeleteMapping(value = "/{brand}")
    void remove(@PathVariable String brandId) {

    }

}
