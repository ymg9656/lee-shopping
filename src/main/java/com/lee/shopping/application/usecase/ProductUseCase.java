package com.lee.shopping.application.usecase;

import com.lee.shopping.application.exception.ApplicationException;
import com.lee.shopping.application.exception.ExceptionCode;
import com.lee.shopping.application.mapper.ProductRequestMapper;
import com.lee.shopping.application.mapper.ProductResponseMapper;
import com.lee.shopping.application.request.ProductRequest;
import com.lee.shopping.application.response.ProductResponse;
import com.lee.shopping.domain.*;
import com.lee.shopping.domain.service.BrandService;
import com.lee.shopping.domain.service.CategoryService;
import com.lee.shopping.domain.service.ProductRankService;
import com.lee.shopping.domain.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductUseCase {
    private final ProductService productService;
    private final ProductRankService productRankService;
    private final BrandService brandService;
    private final CategoryService categoryService;



    public ProductResponse register(ProductRequest request) throws ApplicationException {
        String categoryId= request.getCategory();
        String brandId= request.getBrand();
        //1. 카테고리 존재유무
        Optional.ofNullable(categoryService.findById(categoryId)).orElseThrow(()
                -> new ApplicationException(ExceptionCode.INVALID_REQUEST, "category"));

        //2. 브랜드 존재유무
        Optional.ofNullable(brandService.findById(brandId)).orElseThrow(()
                -> new ApplicationException(ExceptionCode.INVALID_REQUEST, "brand"));

        Product p = ProductRequestMapper.INSTANCE.toProduct(request);
        //1. 상품 등록
        p = productService.register(p);
        long categoryCount = categoryService.count();
        productRankService.update(p,categoryCount);
        return ProductResponseMapper.INSTANCE.to(p);
    }


    public ProductResponse modify(Long productId, ProductRequest request) throws ApplicationException {

        Product dbProduct = Optional.ofNullable(productService.getProductById(productId)).orElseThrow(()
                -> new ApplicationException(ExceptionCode.INVALID_REQUEST, "productId"));

        String categoryId= request.getCategory();
        String brandId= request.getBrand();
        //1. 카테고리 존재유무
        Optional.ofNullable(categoryService.findById(categoryId)).orElseThrow(()
                -> new ApplicationException(ExceptionCode.INVALID_REQUEST, "category"));

        //2. 브랜드 존재유무
        Optional.ofNullable(brandService.findById(brandId)).orElseThrow(()
                -> new ApplicationException(ExceptionCode.INVALID_REQUEST, "brand"));

        Product p = ProductRequestMapper.INSTANCE.toProduct(productId,request);
        productService.register(p);


        boolean equalsBrand=dbProduct.getBrand().getId().equals(p.getBrand().getId());
        boolean equalsCategory=dbProduct.getCategory().getId().equals(p.getCategory().getId());
        long categoryCount = categoryService.count();
        if(!equalsBrand || !equalsCategory){
            //브랜드 or 카테고리가 변경되는 경우 랭크 갱신
            productRankService.deleteAndRefreshRanksByProductId(productId,dbProduct.getBrand().getId(),categoryCount);
        }

        productRankService.update(p,categoryCount);
        return ProductResponseMapper.INSTANCE.to(p);
    }

    public void remove(Long productId) throws ApplicationException {
        Product db = Optional.ofNullable(productService.getProductById(productId)).orElseThrow(()
                -> new ApplicationException(ExceptionCode.INVALID_REQUEST, "productId"));;

        //1. 상품삭제
        productService.remove(productId);

        //2. 집계 삭제
        Long categoryCount = categoryService.count();
        productRankService.deleteAndRefreshRanksByProductId(productId,db.getBrand().getId(),categoryCount);
    }


}
