package com.lee.shopping.application.usecase;

import com.lee.shopping.application.mapper.BrandRequestMapper;
import com.lee.shopping.application.mapper.BrandResponseMapper;
import com.lee.shopping.application.mapper.ProductRequestMapper;
import com.lee.shopping.application.mapper.ProductResponseMapper;
import com.lee.shopping.application.request.BrandRequest;
import com.lee.shopping.application.response.BrandCategoryPrices;
import com.lee.shopping.application.response.BrandResponse;
import com.lee.shopping.application.response.BrandSetLowestResponse;
import com.lee.shopping.domain.Brand;
import com.lee.shopping.domain.Product;
import com.lee.shopping.domain.ProductRank;
import com.lee.shopping.domain.RankKey;
import com.lee.shopping.domain.service.BrandService;
import com.lee.shopping.domain.service.ProductRankService;
import com.lee.shopping.domain.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public BrandResponse register(BrandRequest request) throws Exception {
        Brand p = BrandRequestMapper.INSTANCE.toBrand(request);

        p = brandService.register(p);

        return BrandResponseMapper.INSTANCE.to(p);
    }

    public void remove(String brandId) {
        //TODO
        //삭제시 재집계는 별도 시스템으로 처리 필요
        //1. 삭제되는 데이터가 랭킹에 영향이 있는지 확인 후 영향이 있는 경우 랭킹 집계.
        //- 랭킹에 있는데 삭제되는 경우 후순위열을 앞으로 당겨줘야함.

        brandService.remove(brandId);
        productService.removeAllByBrandId(brandId);
        productRankService.removeAllByBrandId(brandId);

    }
}
