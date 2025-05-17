package com.lee.shopping.application.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    //상품 추가
    @PostMapping
    ResponseEntity register() {
        return ResponseEntity.ok().build();
    }

    //상품 수정
    @PutMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity modify(@PathVariable String productId) {
        return ResponseEntity.ok().build();
    }

    //상품 삭제
    @DeleteMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity remove(@PathVariable String productId) {
        return ResponseEntity.ok().build();
    }
}
