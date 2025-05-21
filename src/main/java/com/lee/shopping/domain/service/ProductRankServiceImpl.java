package com.lee.shopping.domain.service;

import com.lee.shopping.domain.*;
import com.lee.shopping.domain.mapper.ProductRankMapper;
import com.lee.shopping.domain.repository.CategoryRepository;
import com.lee.shopping.domain.repository.ProductRankRepository;
import com.lee.shopping.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductRankServiceImpl implements ProductRankService {

    private final ProductRankRepository productRankRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    //집계 테이블 초기화
    //TODO
    // 별도 시스템으로 초기화 필요
    // SpringBatch, DB Agent.. 등등
    @Override
    public void init() {

        //카테고리별 브랜드 최저가 초기화
        List<Product> lowestForCategory=productRepository.findAllLowestPriceForCategoryRankNoLessThanEqual(RankKey.CATEGORY_LOWEST.getMaxRankSize());
        List<ProductRank> lowestForCategoryRanks = ProductRankMapper.INSTANCE.fromProducts(lowestForCategory, RankKey.CATEGORY_LOWEST);
        productRankRepository.saveAll(lowestForCategoryRanks);

        //카테고리별 브랜드 최고가 초기화
        List<Product> highestForCategory= productRepository.findAllHighestPriceForCategoryRankNoLessThanEqual(RankKey.CATEGORY_LOWEST.getMaxRankSize());
        List<ProductRank> highestForCategoryRanks = ProductRankMapper.INSTANCE.fromProducts(highestForCategory, RankKey.CATEGORY_HIGHEST);
        productRankRepository.saveAll(highestForCategoryRanks);

        //브랜드별 카테고리 최저가 초기화
        List<Product> lowestForBrandAndCategory=productRepository.findAllLowestPriceForBrandAndCategoryRankNoLessThanEqual(RankKey.BRAND_CATEGORY_LOWEST.getMaxRankSize());
        List<ProductRank> lowestForBrandAndCategoryRanks = ProductRankMapper.INSTANCE.fromProducts(lowestForBrandAndCategory, RankKey.BRAND_CATEGORY_LOWEST);
        productRankRepository.saveAll(lowestForBrandAndCategoryRanks);

        //브랜드별 세트 상품 최저가 랭킹
        List<ProductRank> brandSetLowestRanks = productRankRepository.findAllBrandSetLowestCalculate();
        productRankRepository.saveAll(brandSetLowestRanks);

    }


    @Override
    public List<ProductRank> getRanks(RankKey rankKey, int maxRankNo) {
        return productRankRepository.findAllByRankKeyAndRankNoLessThanEqual(rankKey.name(), maxRankNo);
    }

    @Override
    public List<ProductRank> getRanksByBrandId(RankKey rankKey, String brandId, int maxRankNo) {
        return productRankRepository.findAllByRankKeyAndBrandIdAndRankNoLessThanEqual(rankKey.name(),brandId, maxRankNo);
    }

    @Override
    public List<ProductRank> getRanksByBrandIdAndCategoryId(RankKey rankKey, String brandId, String categoryId, int maxRankNo) {
        return productRankRepository.findAllByRankKeyAndBrandIdAndCategoryIdAndRankNoLessThanEqual(rankKey.name(),brandId, categoryId,maxRankNo);
    }



    @Override
    public List<ProductRank> getRanksByCategoryId(RankKey rankKey, String category, int maxRankNo) {
        return productRankRepository.findAllByRankKeyAndCategoryIdAndRankNoLessThanEqual(rankKey.name(),category, maxRankNo);
    }

    @Override
    public List<ProductRank> getRanksByCategoryIdDesc(RankKey rankKey, String category, int maxRankNo) {
        return productRankRepository.findAllByRankKeyAndCategoryIdAndRankNoLessThanEqualRankOrderDesc(rankKey.name(),category, maxRankNo);
    }

    @Override
    public void deleteAllByProductId(Long productId) {
        productRankRepository.deleteAllByProductId(productId);
    }

    @Transactional
    @Override
    public void removeAllByBrandId(String brandId) {
        productRankRepository.deleteAllByBrandId(brandId);
    }


    //TODO
    // 집계 부분은 실 서비스시 변경 필요.
    // 1. Scheduled+SpringBatch를 통한 일정 주기별 집계
    // 2. Apache Kafka Event 방식으로 별도 집계
    // 3. Redis Sorted Set 활용
    @Override
    public void update(Product product) {
        String category = product.getCategory().getId();
        String brand = product.getBrand().getId();

        //2. 랭크 갱신
        //2-1. 카테고리별 브랜드 랭킹
        List<ProductRank> categoryBrandLowestRanks=getRanksByCategoryId(RankKey.CATEGORY_LOWEST,category,RankKey.CATEGORY_LOWEST.getMaxRankSize());
        List<ProductRank> categoryBrandHighestRanks=getRanksByCategoryIdDesc(RankKey.CATEGORY_HIGHEST,category,RankKey.CATEGORY_HIGHEST.getMaxRankSize());
        //DB 조회 동시성 문제가 발생할수 있으니 현재 DB 조회 시점 기준으로 계산해서 등록을 시켜준다.
        //최저가 갱신
        updateLowestRank(product, categoryBrandLowestRanks,RankKey.CATEGORY_LOWEST);
        //최고가 계산
        updateHighestRank(product, categoryBrandHighestRanks,RankKey.CATEGORY_HIGHEST);


        //2-2. 브랜드별 카테고리 최저가 랭킹
        List<ProductRank> brandCategoryLowestRanks = getRanksByBrandIdAndCategoryId(RankKey.BRAND_CATEGORY_LOWEST,brand,category,RankKey.BRAND_CATEGORY_LOWEST.getMaxRankSize());
        updateLowestRank(product, brandCategoryLowestRanks,RankKey.BRAND_CATEGORY_LOWEST);


        updateBrandSetLowestRanks(brand);
        //캐시 갱신

    }
    public void updateBrandSetLowestRanks(String brand){
        //나의 브랜드만 가지고와서 최저가 상품의 합계 계산.
        //근데 카테고리가 상품이 부족한 경우 집계 제외.
        List<ProductRank> myBrandCategoryLowestRanks = getRanksByBrandId(RankKey.BRAND_CATEGORY_LOWEST,brand,1);
        Long categoryCount = categoryRepository.count();
        int brandCategoryCount = myBrandCategoryLowestRanks.size();
        if(brandCategoryCount < categoryCount){
            log.info("updateBrandSetLowestRanks category not enough brandName={},brandCategoryCount={}",brand,brandCategoryCount);
            List<ProductRank> myBrandRank = getRanksByBrandId(RankKey.BRAND_SET_LOWEST,brand,1);
            //카테고리가 부족한데 내랭킹이 있었다면 삭제
            if(!myBrandRank.isEmpty()){
                productRankRepository.delete(myBrandRank.get(0));
            }
            return;
        }
        Product myBrandSetLowest = Product.builder()
                .brand(Brand.builder().id(brand).build())
                .category(Category.builder().id("0").build())
                .id(0L)
                .price(myBrandCategoryLowestRanks.stream().mapToInt(ProductRank::getPrice).sum())
                .build();

        List<ProductRank> brandSetLowestRanks = getRanks(RankKey.BRAND_SET_LOWEST,1);


        //내것만 조회 해서 가격 비교후 업데이트
        updateLowestRank(myBrandSetLowest, brandSetLowestRanks,RankKey.BRAND_SET_LOWEST);

    }

    private void updateLowestRank(Product product, List<ProductRank> ranks,RankKey rankKey) {
        int price = product.getPrice();
        ProductRank newRank = ProductRankMapper.INSTANCE.fromProduct(product,rankKey);

        log.info("updateLowestRank maxRankSize={}, rankSize={}",rankKey.getMaxRankSize(),ranks.size());
        //아직 최대 랭킹 사이즈에 못미치면 그냥 등록
        if(rankKey.getMaxRankSize() > ranks.size()+1){
            productRankRepository.save(newRank);
        }else{
            //최하위 랭크랑 비교해서 조건에 부합하면 등록 및 삭제
            ProductRank oldRank = ranks.get(ranks.size()-1);
            int dbPrice = oldRank.getPrice();
            log.info("updateLowestRank dbPrice={}, paramPrice={}",dbPrice,price);
            if (price < dbPrice) {
                productRankRepository.save(newRank);
                Optional<ProductRank> optionalProductRank = ranks.stream()
                        .filter(e -> e.equals(newRank))
                        .findFirst();

                if(optionalProductRank.isEmpty()){
                    ranks.add(newRank);
                    //신규 등록인 경우 rank max size 초과 햇는지 확인후 마지막 데이터 삭제
                    if(rankKey.getMaxRankSize() < ranks.size()){
                        //가격 낮은순 재정렬
                        ranks.sort(Comparator.comparing(ProductRank::getPrice));
                        productRankRepository.delete(ranks.get(ranks.size()-1));
                        log.info("updateLowestRank delete");
                    }
                }
            }else{
                //랭크에서 제외 되는 경우 삭제
                productRankRepository.delete(newRank);
            }
        }


    }

    private void updateHighestRank(Product product, List<ProductRank> ranks,RankKey rankKey) {
        int price = product.getPrice();
        ProductRank newRank = ProductRankMapper.INSTANCE.fromProduct(product,rankKey);

        log.info("updateHighestRank maxRankSize={}, rankSize={}",rankKey.getMaxRankSize(),ranks.size());
        //아직 최대 랭킹 사이즈에 못미치면 그냥 등록
        if(rankKey.getMaxRankSize() > ranks.size()+1){
            productRankRepository.save(newRank);
        }else{
            //최하위 랭크랑 비교해서 조건에 부합하면 등록 및 삭제
            ProductRank oldRank = ranks.get(ranks.size()-1);
            int dbPrice = oldRank.getPrice();
            log.info("updateLowestRank dbPrice={}, paramPrice={}",dbPrice,price);
            if (price > dbPrice) {
                productRankRepository.save(newRank);
                Optional<ProductRank> optionalProductRank = ranks.stream()
                        .filter(e -> e.equals(newRank))
                        .findFirst();

                if(optionalProductRank.isEmpty()){
                    //신규 등록인 경우 rank max size 초과 햇는지 확인후 마지막 데이터 삭제
                    ranks.add(newRank);
                    if(rankKey.getMaxRankSize() < ranks.size()){
                        //가격 높은순 재정렬
                        ranks.sort(Comparator.comparing(ProductRank::getPrice).reversed());
                        productRankRepository.delete(ranks.get(ranks.size()-1));
                        log.info("updateHighestRank delete");
                    }
                }
            }else{
                productRankRepository.delete(newRank);
            }
        }




    }

}
