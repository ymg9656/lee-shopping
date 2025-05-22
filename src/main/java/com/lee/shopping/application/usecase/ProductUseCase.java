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

import java.util.List;
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
        if(!equalsBrand || !equalsCategory){
            //관련된 집계 테이블 초기화
            updateRank(productId,dbProduct.getBrand().getId());
        }
        long categoryCount = categoryService.count();
        productRankService.update(p,categoryCount);
        return ProductResponseMapper.INSTANCE.to(p);
    }

    public void remove(Long productId) throws ApplicationException {
        Product db = Optional.ofNullable(productService.getProductById(productId)).orElseThrow(()
                -> new ApplicationException(ExceptionCode.INVALID_REQUEST, "productId"));;

        //1. 상품삭제
        productService.remove(productId);

        //2. 집계 삭제
        updateRank(productId,db.getBrand().getId());
    }

    private void updateRank(Long productId, String brandId){
        List<ProductRank> productRanks = productRankService.getRanksByProductId(productId);
        if(productRanks.isEmpty()){
            return;
        }

        productRankService.deleteAllByProductId(productId);
        for(ProductRank rank:productRanks){
            productRankService.reload(rank.getRankKey());
        }
        Long categoryCount = categoryService.count();
        productRankService.updateBrandSetLowestRanks(brandId,categoryCount);

    }
}
