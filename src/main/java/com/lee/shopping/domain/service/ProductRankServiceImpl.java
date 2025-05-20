package com.lee.shopping.domain.service;

import com.lee.shopping.domain.ProductRank;
import com.lee.shopping.domain.Product;
import com.lee.shopping.domain.RankKey;
import com.lee.shopping.domain.mapper.ProductRankMapper;
import com.lee.shopping.domain.repository.ProductRankRepository;
import com.lee.shopping.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.SortDirection;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductRankServiceImpl implements ProductRankService {
    private final ProductRankRepository productRankRepository;
    private final ProductRepository productRepository;
    //집계 테이블 초기화
    //TODO
    // 별도 시스템으로 초기화 필요
    // SpringBatch, DB Agent.. 등등
    @Override
    public void init() {
        //카테고리별 브랜드 최저가 초기화
        List<Product> lowestForCategory=productRepository.findAllLowestPriceForCategory();
        List<ProductRank> lowestForCategoryRanks = ProductRankMapper.INSTANCE.fromProducts(lowestForCategory, RankKey.CATEGORY_BRAND_LOWEST);
        productRankRepository.saveAll(lowestForCategoryRanks);

        //카테고리별 브랜드 최고가 초기화
        List<Product> highestForCategory= productRepository.findAllHighestPriceForCategory();
        List<ProductRank> highestForCategoryRanks = ProductRankMapper.INSTANCE.fromProducts(highestForCategory, RankKey.CATEGORY_BRAND_HIGHEST);
        productRankRepository.saveAll(highestForCategoryRanks);

        //브랜드별 카테고리 최저가 초기화
        List<Product> lowestForBrandAndCategory=productRepository.findAllLowestPriceForBrandAndCategory();
        List<ProductRank> lowestForBrandAndCategoryRanks = ProductRankMapper.INSTANCE.fromProducts(lowestForBrandAndCategory, RankKey.BRAND_CATEGORY_LOWEST);
        productRankRepository.saveAll(lowestForBrandAndCategoryRanks);



        //브랜드별 세트 상품 최저가 랭킹
        List<ProductRank> brandSetLowestRanks = productRankRepository.findAllBrandSetLowestCalculate();
        productRankRepository.saveAll(brandSetLowestRanks);

    }


    @Override
    public List<ProductRank> getRanks(RankKey rankKey, int maxRankNo,SortDirection direction) {
        Integer rankNo=1;
        return productRankRepository.findAllByRankKeyAndRankNoLessThanEqual(rankKey.name(), maxRankNo);
    }

    @Override
    public List<ProductRank> getRanksByCategoryId(RankKey rankKey, String category, int maxRankNo) {
        return productRankRepository.findAllByRankKeyAndCategoryIdAndRankNoLessThanEqual(rankKey.name(),category, maxRankNo);
    }

    @Override
    public List<ProductRank> getRanksByCategoryIdDesc(RankKey rankKey, String category, int maxRankNo) {
        return productRankRepository.findAllByRankKeyAndCategoryIdAndRankNoLessThanEqualRankOrderDesc(rankKey.name(),category, maxRankNo);
    }


    //TODO
    // 집계 부분은 실 서비스시 변경 필요.
    // 1. Scheduled+SpringBatch를 통한 일정 주기별 집계
    // 2. Apache Kafka Event 방식으로 별도 집계
    // 3. Redis Sorted Set 활용
    @Override
    public void update(Product product) {
        //해당 카테고리 최저가/최고가 랭킹 데이터 조회

        //최저가 랭킹 필터
        List<ProductRank> lowestRanks = productRankRepository.findAllByRankKeyAndCategoryIdAndRankNoLessThanEqual(RankKey.CATEGORY_BRAND_LOWEST.name(),product.getCategory().getId(),1);

        //최고가 랭킹 필터
        List<ProductRank> highestRanks = productRankRepository.findAllByRankKeyAndCategoryIdAndRankNoLessThanEqualRankOrderDesc(RankKey.CATEGORY_BRAND_HIGHEST.name(),product.getCategory().getId(),1);


        //DB 조회 동시성 문제가 발생할수 있으니 현재 DB 조회 시점 기준으로 계산해서 등록을 시켜준다.
        //최저가 갱신
        updateLowestRank(product, lowestRanks);
        //최고가 계산
        updateHighestRank(product, highestRanks);

        //캐시 갱신


    }




    private void updateLowestRank(Product product, List<ProductRank> ranks) {
        int price = product.getPrice();

        int dbLowestPrice = ranks.isEmpty() ? 999_999_999 : ranks.get(0).getPrice();

        if (price < dbLowestPrice) {
            /*ProductRank newRank = ProductRank.builder()
                    .productId(product)
                    .rankKey(RankKey.CATEGORY_BRAND_LOWEST)
                    .build();
            productRankRepository.save(newRank);*/
        }
    }

    private void updateHighestRank(Product product, List<ProductRank> ranks) {
        int price = product.getPrice();
        int dbHighestPrice = ranks.isEmpty() ? 0 : ranks.get(0).getPrice();

        if (price > dbHighestPrice) {
           /* ProductRank newRank = ProductRank.builder()
                    .product(product)
                    .rankKey(RankKey.CATEGORY_BRAND_HIGHEST)
                    .build();
            productRankRepository.save(newRank);*/
        }
    }
}
