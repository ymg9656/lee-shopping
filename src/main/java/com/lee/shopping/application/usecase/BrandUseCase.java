package com.lee.shopping.application.usecase;

import com.lee.shopping.application.exception.ApplicationException;
import com.lee.shopping.application.exception.ExceptionCode;
import com.lee.shopping.application.mapper.BrandRequestMapper;
import com.lee.shopping.application.mapper.BrandResponseMapper;
import com.lee.shopping.application.mapper.ProductResponseMapper;
import com.lee.shopping.application.request.BrandRequest;
import com.lee.shopping.application.response.BrandCategoryPrices;
import com.lee.shopping.application.response.BrandResponse;
import com.lee.shopping.application.response.BrandSetLowestResponse;
import com.lee.shopping.domain.Brand;
import com.lee.shopping.domain.ProductRank;
import com.lee.shopping.domain.RankKey;
import com.lee.shopping.domain.service.BrandService;
import com.lee.shopping.domain.service.ProductRankService;
import com.lee.shopping.domain.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BrandUseCase {

    private final BrandService brandService;
    private final ProductService productService;
    private final ProductRankService productRankService;

    public BrandSetLowestResponse getBrandSetLowest() {
        //1. 브랜드별 랭킹 조회
        List<ProductRank> ranks = productRankService.getRanks(RankKey.BRAND_SET_LOWEST,1);
        if(ranks.isEmpty()){
            //값이 없는 경우 빈값 제공
            return BrandSetLowestResponse
                    .builder()
                    .lowest(BrandCategoryPrices.builder().build())
                    .build();
        }
        String brandId= ranks.get(0).getBrandId();

        //2. 1등 브랜드의 상품 조회
        List<ProductRank> brandCategoryRanks = productRankService.getRanksByBrandId(RankKey.BRAND_CATEGORY_LOWEST,brandId,1);


        return BrandSetLowestResponse.builder()
                .lowest(BrandCategoryPrices.builder()
                        .brand(brandId)
                        .categories(ProductResponseMapper.INSTANCE.toCategoryPrices(brandCategoryRanks))
                        .totalPrice(ProductResponseMapper.INSTANCE.calculateTotalPrice(brandCategoryRanks))
                        .build())
                .build();
    }

    public BrandResponse register(BrandRequest request) {

        Brand p = BrandRequestMapper.INSTANCE.toBrand(request);

        Optional.ofNullable(brandService.findById(request.getBrand()))
                .ifPresent(brand -> {
                    throw new ApplicationException(ExceptionCode.DUPLICATE, request.getBrand());
                });

        p = brandService.register(p);

        return BrandResponseMapper.INSTANCE.to(p);
    }

    public void remove(String brandId) {

        Optional.ofNullable(brandService.findById(brandId)).orElseThrow(()
                -> new ApplicationException(ExceptionCode.INVALID_REQUEST,"brand"));

        brandService.remove(brandId);
        productService.removeAllByBrandId(brandId);
        productRankService.removeAllByBrandId(brandId);
        productRankService.init();
    }
}
