package com.lee.shopping.application.rest;

import com.lee.shopping.application.request.ProductRequest;
import com.lee.shopping.application.response.ProductResponse;
import com.lee.shopping.domain.service.ProductRankService;
import com.lee.shopping.domain.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ProductRankService productRankService;



    //상품 수정
    @PutMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProductResponse> modify(@PathVariable String productId,@RequestBody ProductRequest request) {
        return ResponseEntity.ok().build();
    }

    //상품 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    @DeleteMapping(value = "/{productId}")
    void remove(@PathVariable String productId) {

    }
}
