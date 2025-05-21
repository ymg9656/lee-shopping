package com.lee.shopping.application.rest;

import com.lee.shopping.application.request.BrandRequest;
import com.lee.shopping.application.request.ProductRequest;
import com.lee.shopping.application.response.BrandSetLowestResponse;
import com.lee.shopping.application.response.BrandResponse;
import com.lee.shopping.application.response.ProductResponse;
import com.lee.shopping.application.usecase.BrandUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandUseCase brandUseCase;


    //단일 브랜드로 모든 카테고리 상품을 최저가격으로 구매할 때의 정보 조회
    @GetMapping(value = "/categories/lowest")
    ResponseEntity<BrandSetLowestResponse> getBrandCategoriesLowest() {
        return ResponseEntity.ok(brandUseCase.getBrandSetLowest());
    }

    //브랜드 추가
    @PostMapping
    ResponseEntity<BrandResponse> register(@RequestBody BrandRequest request) throws Exception {
        return ResponseEntity.ok(brandUseCase.register(request));
    }

    //브랜드 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    @DeleteMapping(value = "/{brandId}")
    void remove(@PathVariable String brandId) {
        brandUseCase.remove(brandId);
    }

}
