package com.lee.shopping.domain.service;

import com.lee.shopping.domain.*;
import com.lee.shopping.domain.mapper.ProductRankMapper;
import com.lee.shopping.domain.repository.ProductRankRepository;
import com.lee.shopping.domain.repository.ProductRepository;
import com.lee.shopping.infrastracture.cache.CacheNames;
import com.lee.shopping.infrastracture.cache.LocalCacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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
    private final LocalCacheManager localCacheManager;





    //집계 테이블 초기화
    //TODO
    // 별도 시스템으로 초기화 필요
    // SpringBatch, DB Agent.. 등등
    @Override
    public void init() {
        reload(RankKey.CATEGORY_LOWEST);
        reload(RankKey.CATEGORY_HIGHEST);
        reload(RankKey.BRAND_CATEGORY_LOWEST);
        reload(RankKey.BRAND_SET_LOWEST);
    }

    //카테고리별 브랜드 최저가 초기화
    private void initCategoryLowest(){
        List<Product> lowestForCategory=productRepository.findAllLowestPriceForCategoryRankNoLessThanEqual(RankKey.CATEGORY_LOWEST.getMaxRankSize());
        List<ProductRank> lowestForCategoryRanks = ProductRankMapper.INSTANCE.fromProducts(lowestForCategory, RankKey.CATEGORY_LOWEST);
        productRankRepository.saveAll(lowestForCategoryRanks);
        log.info("initCategoryLowest count={}",lowestForCategoryRanks.size());
    }
    //카테고리별 브랜드 최고가 초기화
    private void initCategoryHighest(){
        List<Product> highestForCategory= productRepository.findAllHighestPriceForCategoryRankNoLessThanEqual(RankKey.CATEGORY_LOWEST.getMaxRankSize());
        List<ProductRank> highestForCategoryRanks = ProductRankMapper.INSTANCE.fromProducts(highestForCategory, RankKey.CATEGORY_HIGHEST);
        productRankRepository.saveAll(highestForCategoryRanks);
        log.info("initCategoryHighest count={}",highestForCategoryRanks.size());
    }
    //브랜드별 카테고리 최저가 초기화
    private void initBrandCategoryLowest(){
        List<Product> lowestForBrandAndCategory=productRepository.findAllLowestPriceForBrandAndCategoryRankNoLessThanEqual(RankKey.BRAND_CATEGORY_LOWEST.getMaxRankSize());
        List<ProductRank> lowestForBrandAndCategoryRanks = ProductRankMapper.INSTANCE.fromProducts(lowestForBrandAndCategory, RankKey.BRAND_CATEGORY_LOWEST);
        productRankRepository.saveAll(lowestForBrandAndCategoryRanks);
        log.info("initBrandCategoryLowest count={}",lowestForBrandAndCategoryRanks.size());
    }
    //브랜드별 세트 상품 최저가 랭킹
    private void initBrandSetLowest(){
        List<ProductRank> brandSetLowestRanks = productRankRepository.findAllBrandSetLowestCalculate();
        productRankRepository.saveAll(brandSetLowestRanks);
        log.info("initBrandSetLowest count={}",brandSetLowestRanks.size());
    }
    public void reload(RankKey rankKey){

        switch (rankKey) {
            case CATEGORY_LOWEST:
                initCategoryLowest(); break;
            case CATEGORY_HIGHEST:
                initCategoryHighest(); break;
            case BRAND_CATEGORY_LOWEST:
                initBrandCategoryLowest(); break;
            case BRAND_SET_LOWEST:
                initBrandSetLowest(); break;
        }

        localCacheManager.cacheEvict(CacheNames.CAFFEINE_RANKS_TTL_5M,rankKey.name());
    }
    @Cacheable(value = {CacheNames.CAFFEINE_RANKS_TTL_5M}, key = "#rankKey.name()+':'+#maxRankNo", unless = "#result.isEmpty()")
    @Override
    public List<ProductRank> getRanks(RankKey rankKey, int maxRankNo) {
        return productRankRepository.findAllByRankKeyAndRankNoLessThanEqual(rankKey.name(), maxRankNo);
    }

    @Cacheable(value = {CacheNames.CAFFEINE_RANKS_TTL_5M}, key = "#rankKey.name()+':brands:'+#brandId+':'+#maxRankNo", unless = "#result.isEmpty()")
    @Override
    public List<ProductRank> getRanksByBrandId(RankKey rankKey, String brandId, int maxRankNo) {
        return productRankRepository.findAllByRankKeyAndBrandIdAndRankNoLessThanEqual(rankKey.name(),brandId, maxRankNo);
    }

    @Override
    public List<ProductRank> getRanksByBrandIdAndCategoryId(RankKey rankKey, String brandId, String categoryId, int maxRankNo) {
        return productRankRepository.findAllByRankKeyAndBrandIdAndCategoryIdAndRankNoLessThanEqual(rankKey.name(),brandId, categoryId,maxRankNo);
    }


    @Cacheable(value = {CacheNames.CAFFEINE_RANKS_TTL_5M}, key = "#rankKey.name()+':categories:'+#category+':'+#maxRankNo", unless = "#result.isEmpty()")
    @Override
    public List<ProductRank> getRanksByCategoryId(RankKey rankKey, String category, int maxRankNo) {
        return productRankRepository.findAllByRankKeyAndCategoryIdAndRankNoLessThanEqual(rankKey.name(),category, maxRankNo);
    }

    @Cacheable(value = {CacheNames.CAFFEINE_RANKS_TTL_5M}, key = "#rankKey.name()+':categories:'+#category+':'+#maxRankNo", unless = "#result.isEmpty()")
    @Override
    public List<ProductRank> getRanksByCategoryIdDesc(RankKey rankKey, String category, int maxRankNo) {
        return productRankRepository.findAllByRankKeyAndCategoryIdAndRankNoLessThanEqualRankOrderDesc(rankKey.name(),category, maxRankNo);
    }
    @Override
    public List<ProductRank> getRanksByProductId(Long productId) {
        return productRankRepository.findAllByProductId(productId);
    }

    //TODO
    // 집계 부분은 실 서비스시 변경 필요.
    // 1. Scheduled+SpringBatch를 통한 일정 주기별 집계
    // 2. Apache Kafka Event 방식으로 별도 집계
    // 3. Redis Sorted Set 활용
    // 4. 캐시 매니저를 전체 삭제가 아닌 해당 키만 갱신 해주는걸로
    @Override
    public void update(Product product,long categoryCount) {
        String category = product.getCategory().getId();
        String brand = product.getBrand().getId();

        //랭크 갱신은 DB 조회 동시성 문제가 발생할수 있으니 랭크 버퍼를 두고 업데이트 시켜준다.
        //1. 카테고리별 브랜드 최저가 랭킹
        List<ProductRank> categoryBrandLowestRanks=getRanksByCategoryId(RankKey.CATEGORY_LOWEST,category,RankKey.CATEGORY_LOWEST.getMaxRankSize());
        updateLowestRank(product, categoryBrandLowestRanks,RankKey.CATEGORY_LOWEST);

        //2. 카테고리별 브랜드 최고가 랭킹
        List<ProductRank> categoryBrandHighestRanks=getRanksByCategoryIdDesc(RankKey.CATEGORY_HIGHEST,category,RankKey.CATEGORY_HIGHEST.getMaxRankSize());
        updateHighestRank(product, categoryBrandHighestRanks,RankKey.CATEGORY_HIGHEST);

        //3. 브랜드별 카테고리 최저가 랭킹
        List<ProductRank> brandCategoryLowestRanks = getRanksByBrandIdAndCategoryId(RankKey.BRAND_CATEGORY_LOWEST,brand,category,RankKey.BRAND_CATEGORY_LOWEST.getMaxRankSize());
        updateLowestRank(product, brandCategoryLowestRanks,RankKey.BRAND_CATEGORY_LOWEST);

        //4. 브랜드별 카테고리 합계 최저가 랭킹
        updateBrandSetLowestRanks(brand,categoryCount);
    }
    public void updateBrandSetLowestRanks(String brand, long totalCategoryCount){
        //나의 브랜드만 가지고와서 최저가 상품의 합계 계산.
        //근데 카테고리가 상품이 부족한 경우 집계 제외.
        List<ProductRank> myBrandCategoryLowestRanks = getRanksByBrandId(RankKey.BRAND_CATEGORY_LOWEST,brand,1);
        int brandCategoryCount = myBrandCategoryLowestRanks.size();
        if(brandCategoryCount < totalCategoryCount){
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

        List<ProductRank> brandSetLowestRanks = getRanks(RankKey.BRAND_SET_LOWEST,RankKey.BRAND_SET_LOWEST.getMaxRankSize());


        updateLowestRank(myBrandSetLowest, brandSetLowestRanks,RankKey.BRAND_SET_LOWEST);

    }


    private boolean isOnlyMyRankExists(ProductRank product, List<ProductRank> ranks){

        Optional<ProductRank> optionalHasRank = ranks.stream()
                .filter(e -> e.equals(product))
                .findFirst();

        return ranks.size()==1 && optionalHasRank.isPresent();
    }
    private void updateLowestRank(Product product, List<ProductRank> ranks,RankKey rankKey) {
        if(rankKey.getMaxRankSize() > ranks.size()){
            //집계된 랭킹 순위가 아직 모자르다면 상품 테이블 기준으로 재집계.
            log.info("updateRank key={}, ranksSize={}",rankKey.name(),ranks.size());
            reload(rankKey);
            return;
        }
        int price = product.getPrice();
        ProductRank newRank = ProductRankMapper.INSTANCE.fromProduct(product, rankKey);
        int bottomPrice = ranks.isEmpty() ? 99999999 : ranks.get(ranks.size()-1).getPrice();

        //기존 랭크에 내 랭크만 있는 경우 업데이트 후 종료
        if(isOnlyMyRankExists(newRank, ranks)){
            save(newRank);
        } else if (price < bottomPrice) {
            saveAndReorderRanks(ranks, newRank, Comparator.comparing(ProductRank::getPrice)
                    .thenComparing(Comparator.comparing(ProductRank::getProductId)));
        } else {
            deleteIfExists(ranks, newRank);
        }

    }

    private void updateHighestRank(Product product, List<ProductRank> ranks,RankKey rankKey) {
        if(rankKey.getMaxRankSize() > ranks.size()){
            //집계된 랭킹 순위가 아직 모자르다면 상품 테이블 기준으로 재집계.
            log.info("updateRank key={}, ranksSize={}",rankKey.name(),ranks.size());
            reload(rankKey);
            return;
        }
        int price = product.getPrice();
        ProductRank newRank = ProductRankMapper.INSTANCE.fromProduct(product, rankKey);
        int bottomPrice = ranks.isEmpty() ? 0 : ranks.get(ranks.size() - 1).getPrice();

        //기존 랭크에 내 랭크만 있는 경우 업데이트 후 종료
        if(isOnlyMyRankExists(newRank, ranks)){
            save(newRank);
        } else if (price > bottomPrice) {
            saveAndReorderRanks(ranks, newRank, Comparator.comparing(ProductRank::getPrice).reversed()
                    .thenComparing(Comparator.comparing(ProductRank::getProductId)));
        } else {
            deleteIfExists(ranks, newRank);
        }
    }



    @Override
    public void remove(ProductRank productRank) {
        productRankRepository.delete(productRank);

    }
    @Override
    public void removeAllByProductId(Long productId) {
        productRankRepository.deleteAllByProductId(productId);
    }



    @Transactional
    @Override
    public void removeAllByBrandId(String brandId) {
        productRankRepository.deleteAllByBrandId(brandId);
    }
    @Override
    public void removeAndRefreshRanksByProductId(Long productId, String brandId, Long categoryCount) {
        List<ProductRank> productRanks = getRanksByProductId(productId);
        if(productRanks.isEmpty()){
            return;
        }
        //여기에서 트랜잭션
        removeAllByProductId(productId);

        for(ProductRank rank:productRanks){
            reload(rank.getRankKey());
            if(rank.getRankKey()==RankKey.BRAND_CATEGORY_LOWEST){
                updateBrandSetLowestRanks(brandId,categoryCount);
            }
        }
    }
    private void save(ProductRank newRank){
        productRankRepository.save(newRank);
        localCacheManager.cacheEvict(CacheNames.CAFFEINE_RANKS_TTL_5M,newRank.getRankKey().name());
    }

    private void saveAndReorderRanks(List<ProductRank> ranks, ProductRank newRank, Comparator<ProductRank> comparator) {

        Optional<ProductRank> optionalHasRank = ranks.stream()
                .filter(e -> e.equals(newRank))
                .findFirst();

        if (optionalHasRank.isEmpty()) {
            ranks.add(newRank);
            ranks.sort(comparator);
            productRankRepository.delete(ranks.get(ranks.size() - 1));
        }
        save(newRank);
    }

    private void deleteIfExists(List<ProductRank> ranks, ProductRank newRank) {
        Optional<ProductRank> optionalHasRank = ranks.stream()
                .filter(e -> e.equals(newRank))
                .findFirst();
        //기존에 랭크에 포함된 경우 삭제
        if (optionalHasRank.isPresent()) {
            productRankRepository.delete(newRank);
            localCacheManager.cacheEvict(CacheNames.CAFFEINE_RANKS_TTL_5M,newRank.getRankKey().name());
        }
    }

}
