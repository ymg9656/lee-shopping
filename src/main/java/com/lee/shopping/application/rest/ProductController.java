package com.lee.shopping.application.rest;

import com.lee.shopping.application.request.ProductRequest;
import com.lee.shopping.application.response.ProductResponse;
import com.lee.shopping.application.usecase.ProductUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductUseCase productUseCase;

    //상품 추가
    @PostMapping
    ResponseEntity<ProductResponse> register(@RequestBody @Valid ProductRequest request) throws Exception {

        return ResponseEntity.ok(productUseCase.register(request));
    }

    //상품 수정
    @PutMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProductResponse> modify(@PathVariable Long productId, @RequestBody ProductRequest request) throws Exception {
        return ResponseEntity.ok(productUseCase.modify(productId, request));
    }

    //상품 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    @DeleteMapping(value = "/{productId}")
    void remove(@PathVariable Long productId) throws Exception {
        productUseCase.remove(productId);
    }
}
