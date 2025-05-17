package com.lee.shopping.application.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/brands")
public class BrandController {

    //단일 브랜드로 모든 카테고리 상품을 최저가격으로 구매할 때의 정보 조회
    @GetMapping(value = "/{brandId}/categories/lowest")
    ResponseEntity brendCategoriesLowest(@PathVariable String brandId) {
        return ResponseEntity.ok().build();
    }
    //브랜드 추가
    @PostMapping
    ResponseEntity register() {
        return ResponseEntity.ok().build();
    }
    //브랜드 수정
    @PutMapping(value = "/{brandId}")
    ResponseEntity modify(@PathVariable String brandId) {
        return ResponseEntity.ok().build();
    }
    //브랜드 삭제
    @DeleteMapping(value = "/{brandId}")
    ResponseEntity remove(@PathVariable String brandId) {
        return ResponseEntity.ok().build();
    }

}
