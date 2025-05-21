package com.lee.shopping.application.usecase;

import com.lee.shopping.application.exception.ApplicationException;
import com.lee.shopping.application.mapper.ProductRequestMapper;
import com.lee.shopping.application.mapper.ProductResponseMapper;
import com.lee.shopping.application.request.ProductRequest;
import com.lee.shopping.application.response.ProductResponse;
import com.lee.shopping.domain.Product;
import com.lee.shopping.domain.ProductRank;
import com.lee.shopping.domain.RankKey;
import com.lee.shopping.domain.service.ProductRankService;
import com.lee.shopping.domain.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductUseCase {
    private final ProductService productService;
    private final ProductRankService productRankService;


    public ProductResponse register(ProductRequest request) throws ApplicationException {
        Product p = ProductRequestMapper.INSTANCE.toProduct(request);

        //1. 상품 등록
        p = productService.register(p);

        productRankService.update(p);


        return ProductResponseMapper.INSTANCE.to(p);
    }


    public ProductResponse modify(Long productId, ProductRequest request) throws Exception {
        Product p = ProductRequestMapper.INSTANCE.toProduct(productId,request);
        Optional<Product> db = productService.getProductById(productId);
        if (db.isEmpty()) {
            //상품이 존재 하지 않음.
            throw new Exception();
        }
        productService.register(p);

        Product dbProduct = db.get();
        boolean equalsBrand=dbProduct.getBrand().getId().equals(p.getBrand().getId());
        boolean equalsCategory=dbProduct.getCategory().getId().equals(p.getCategory().getId());
        if(!equalsBrand || !equalsCategory){
            //브랜드나 카테고리 변경된게 있다면
            //관련된 집계 테이블 초기화
            productRankService.deleteAllByProductId(productId);
        }
        productRankService.update(p);
        return ProductResponseMapper.INSTANCE.to(p);
    }

    public void remove(Long productId) throws Exception {
        Optional<Product> db = productService.getProductById(productId);
        if (db.isEmpty()) {
            //상품이 존재 하지 않음.
            throw new Exception();
        }
        //1. 상품삭제
        productService.remove(productId);


        //2. 집계 삭제
        productRankService.deleteAllByProductId(productId);

        //TODO
        //삭제시 재집계는 별도 시스템으로 처리 필요

        //3. summary 재집계
        productRankService.updateBrandSetLowestRanks(db.get().getBrand().getId());
    }
}
