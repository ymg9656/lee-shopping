package com.lee.shopping.application.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    //카테고리별 최저가격 브랜드와 상품 가격, 총액 조회
    @GetMapping(value = "/lowest")
    ResponseEntity categoriesLowest() {
        return ResponseEntity.ok().build();
    }

    //카테고리별 최저가격 브랜드와 상품 가격, 총액 조회
    @GetMapping(value = "/{categoryId}")
    ResponseEntity detail(@PathVariable String categoryId) {
        return ResponseEntity.ok().build();
    }

    //카테고리 추가
    @PostMapping
    ResponseEntity register() {
        return ResponseEntity.ok().build();
    }
    //카테고리 수정
    @PutMapping(value = "/{categoryId}")
    ResponseEntity modify(@PathVariable String categoryId) {
        return ResponseEntity.ok().build();
    }
    //카테고리 삭제
    @DeleteMapping(value = "/{categoryId}")
    ResponseEntity remove(@PathVariable String categoryId) {
        return ResponseEntity.ok().build();
    }

}
