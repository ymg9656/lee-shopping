package com.lee.shopping.domain.service;

import com.lee.shopping.domain.*;
import com.lee.shopping.domain.repository.CategoryRepository;
import com.lee.shopping.domain.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Slf4j
@SpringBootTest
class ProductRankServiceTest {
    @Autowired
    private ProductRankService productRankService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void clearAllCaches() {
        cacheManager.getCacheNames().forEach(name -> {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        });
    }

    private Product createProduct(String brandId, String categoryId, int price) {
        return Product.builder()
                .brand(Brand.builder().id(brandId).build())
                .category(Category.builder().id(categoryId).build())
                .price(price)
                .build();
    }

    @Test
    void testRankAddUpdate() {

        // given - A브랜드로 모든 카테고리 상품 최저값으로 셋팅
        String brandId = "A";
        List<Category> categories = categoryRepository.findAll();
        List<Product> r = new ArrayList<>();
        for (Category category : categories) {
            int price = 1;
            r.add(createProduct(brandId, category.getId(), price));
        }
        Long categoryCount = categoryRepository.count();
        r = productRepository.saveAll(r);


        // when 랭크 업데이트
        for (Product p : r) {
            productRankService.update(p, categoryCount);
        }

        // then 검증
        // 모든 카테고리 A 브랜드가 최저가 랭킹 1순위여야 한다.
        List<ProductRank> categoryLowestRanks = productRankService.getRanks(RankKey.CATEGORY_LOWEST, 1);
        assertThat(categoryLowestRanks)
                .isNotEmpty()
                .allMatch(product -> brandId.equals(product.getBrandId()));


        // A 브랜드 모든 카테고리 최저가 금액이 1이어야 한다.
        List<ProductRank> brandCategoryLowestRanks = productRankService.getRanksByBrandId(RankKey.BRAND_CATEGORY_LOWEST, brandId, 1);
        assertThat(brandCategoryLowestRanks)
                .isNotEmpty()
                .allMatch(product -> 1 == product.getPrice());


        // 브랜드 카테고리 최저가합 랭킹 1순위여야 한다.
        List<ProductRank> brandSetLowestRanks = productRankService.getRanks(RankKey.BRAND_SET_LOWEST, 1);
        ProductRank topRank = brandSetLowestRanks.get(0);

        assertThat(topRank.getBrandId()).isEqualTo(brandId);
        assertThat(topRank.getPrice()).isEqualTo(categoryCount * 1);

    }


    @Test
    void testRankDelUpdate() {

        // given - 1순위 랭크 생성
        String brandId = "A";
        String category = "상의";
        int price = 1;
        Long categoryCount = categoryRepository.count();
        Product p = createProduct(brandId, category, price);
        p = productRepository.save(p);
        productRankService.update(p, categoryCount);
        productRepository.deleteById(p.getId());

        ProductRank beforeRank1 = productRankService.getRanksByCategoryId(RankKey.CATEGORY_LOWEST, category, 1).get(0);
        log.info("beforeCategoryLowestRanks brandId={},categoryId={},productId={},price={}", beforeRank1.getBrandId(), beforeRank1.getCategoryId(), beforeRank1.getProductId(), beforeRank1.getPrice());

        ProductRank beforeRank2 = productRankService.getRanksByCategoryId(RankKey.BRAND_CATEGORY_LOWEST, category, 1).get(0);
        log.info("beforeBrandCategoryLowestRank brandId={},categoryId={},productId={},price={}", beforeRank2.getBrandId(), beforeRank2.getCategoryId(), beforeRank2.getProductId(), beforeRank2.getPrice());

        ProductRank beforeRank3 = productRankService.getRanks(RankKey.BRAND_SET_LOWEST, 1).get(0);
        log.info("beforeBrandSetLowestRank brandId={},categoryId={},productId={},price={}", beforeRank3.getBrandId(), beforeRank3.getCategoryId(), beforeRank3.getProductId(), beforeRank3.getPrice());


        // when 랭크 삭제 및 갱신
        productRankService.removeAndRefreshRanksByProductId(p.getId(), brandId, categoryCount);


        // then 검증
        // 1순위 랭크가 내가 아니어야 함.
        ProductRank afterRank1 = productRankService.getRanksByCategoryId(RankKey.CATEGORY_LOWEST, category, 1).get(0);
        log.info("afterCategoryLowestRanks brandId={},categoryId={},productId={},price={}", afterRank1.getBrandId(), afterRank1.getCategoryId(), afterRank1.getProductId(), afterRank1.getPrice());
        assertThat(beforeRank1).isNotEqualTo(afterRank1);

        ProductRank afterRank2 = productRankService.getRanksByCategoryId(RankKey.BRAND_CATEGORY_LOWEST, category, 1).get(0);
        log.info("afterBrandCategoryLowestRank brandId={},categoryId={},productId={},price={}", afterRank2.getBrandId(), afterRank2.getCategoryId(), afterRank2.getProductId(), afterRank2.getPrice());
        assertThat(beforeRank2).isNotEqualTo(afterRank2);

        ProductRank afterRank3 = productRankService.getRanks(RankKey.BRAND_SET_LOWEST, 1).get(0);
        log.info("afterBrandSetLowestRank brandId={},categoryId={},productId={},price={}", afterRank3.getBrandId(), afterRank3.getCategoryId(), afterRank3.getProductId(), afterRank3.getPrice());
        assertThat(beforeRank3).isNotEqualTo(afterRank3);


    }


    //랭킹이 아직 여유가 있는 경우. 상품 테이블 기준 스코어로 재정렬하여 등록이 되는지?
    @Test
    void testRankAddReloadTest() {
        // given
        String category = "상의";
        String brandId = "A";
        int price = 1;
        int maxSize = RankKey.CATEGORY_LOWEST.getMaxRankSize();

        // 현재 랭킹 인원을 제한 인원 이하로 조정
        List<ProductRank> currentRanks = productRankService.getRanksByCategoryId(RankKey.CATEGORY_LOWEST, category, maxSize);
        int removedCount = removeRanksToLimit(currentRanks, maxSize);

        // 신규 상품 등록
        Long categoryCount = categoryRepository.count();
        Product product = createProduct(brandId, category, price);
        product = productRepository.save(product);

        // when: 랭킹 반영
        productRankService.update(product, categoryCount);

        // then: 예상 랭킹 사이즈 계산 및 검증
        List<ProductRank> updatedRanks = productRankService.getRanksByCategoryId(RankKey.CATEGORY_LOWEST, category, maxSize);

        // 기존 랭킹 사이즈 - 제거된 수 + 1개 추가 → 하지만 최대 maxSize까지만 유지
        int expectedSize = Math.min(maxSize, currentRanks.size() - removedCount + 1);
        assertThat(updatedRanks).hasSize(expectedSize);

    }

    private int removeRanksToLimit(List<ProductRank> ranks, int maxSize) {
        int removed = 0;
        if (ranks.size() >= maxSize) {
            for (int i = ranks.size() - 1; i >= maxSize - 1; i--) {
                productRankService.remove(ranks.get(i));
                removed++;
            }
        }
        return removed;
    }


    //랭킹 제한 인원이 가득찬 경우+점수가 높은 경우 순위 갱신 되는지.
    @Test
    void testRankAddWhenRankLimitHighScoreTest() {
        // given
        String category = "상의";
        String brandId = "A";
        int price = 1;
        int maxSize = RankKey.CATEGORY_LOWEST.getMaxRankSize();
        // 랭킹이 가득 찬 상태 확인
        List<ProductRank> beforeRanks = productRankService.getRanksByCategoryId(RankKey.CATEGORY_LOWEST, category, maxSize);
        assertThat(beforeRanks).hasSize(maxSize);

        Long lowestProductIdBefore = beforeRanks.get(maxSize - 1).getProductId();//최하위 아이디
        //log.info("lowestProductIdBefore "+lowestProductIdBefore);
        // 신규 상품 등록 (더 높은 점수)
        Product topProduct = createProduct(brandId, category, price);
        topProduct = productRepository.save(topProduct);

        // when
        productRankService.update(topProduct, categoryRepository.count());

        // then
        List<ProductRank> updatedRanks = productRankService.getRanksByCategoryId(
                RankKey.CATEGORY_LOWEST, category, maxSize);

        // 1. 새로 등록한 상품이 1위인지
        assertThat(updatedRanks.get(0).getProductId()).isEqualTo(topProduct.getId());

        // 2. 기존 최하위 상품이 랭크에서 빠졌는지
        boolean lowestStillExists = updatedRanks.stream()
                .anyMatch(rank -> rank.getProductId().equals(lowestProductIdBefore));
        assertThat(lowestStillExists).isFalse();

        // 3. 랭킹 사이즈는 그대로 유지
        assertThat(updatedRanks).hasSize(maxSize);


    }

}