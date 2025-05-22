package com.lee.shopping.domain.service;

import com.lee.shopping.domain.*;
import com.lee.shopping.domain.repository.CategoryRepository;
import com.lee.shopping.domain.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        String brandId="A";
        List<Category> categories = categoryRepository.findAll();
        List<Product> r= new ArrayList<>();
        for(Category category:categories){
            int price = 1;
            r.add(createProduct(brandId,category.getId(),price));
        }
        Long categoryCount = categoryRepository.count();
        r = productRepository.saveAll(r);


        // when 랭크 업데이트
        for(Product p : r){
            productRankService.update(p,categoryCount);
        }

        // then 검증
        // 모든 카테고리 A 브랜드가 최저가 랭킹 1순위여야 한다.
        List<ProductRank> categoryLowestRanks = productRankService.getRanks(RankKey.CATEGORY_LOWEST,1);
        assertThat(categoryLowestRanks)
                .isNotEmpty()
                .allMatch(product -> brandId.equals(product.getBrandId()));


        // A 브랜드 모든 카테고리 최저가 금액이 1이어야 한다.
        List<ProductRank> brandCategoryLowestRanks = productRankService.getRanksByBrandId(RankKey.BRAND_CATEGORY_LOWEST,brandId,1);
        assertThat(brandCategoryLowestRanks)
                .isNotEmpty()
                .allMatch(product -> 1 == product.getPrice());


        // 브랜드 카테고리 최저가합 랭킹 1순위여야 한다.
        List<ProductRank> brandSetLowestRanks = productRankService.getRanks(RankKey.BRAND_SET_LOWEST,1);
        ProductRank topRank = brandSetLowestRanks.get(0);

        assertThat(topRank.getBrandId()).isEqualTo(brandId);
        assertThat(topRank.getPrice()).isEqualTo(categoryCount * 1);

    }


    @Test
    void testRankDelUpdate() {

        // given - 1순위 랭크 생성
        String brandId="A";
        String category="상의";
        int price = 1;
        Long categoryCount = categoryRepository.count();
        Product p = createProduct(brandId,category,price);
        p = productRepository.save(p);
        productRankService.update(p,categoryCount);
        productRepository.deleteById(p.getId());

        ProductRank beforeRank1 = productRankService.getRanksByCategoryId(RankKey.CATEGORY_LOWEST,category,1).get(0);
        log.info("beforeCategoryLowestRanks brandId={},categoryId={},productId={},price={}",beforeRank1.getBrandId(),beforeRank1.getCategoryId(),beforeRank1.getProductId(),beforeRank1.getPrice());

        ProductRank beforeRank2 = productRankService.getRanksByCategoryId(RankKey.BRAND_CATEGORY_LOWEST,category,1).get(0);
        log.info("beforeBrandCategoryLowestRank brandId={},categoryId={},productId={},price={}",beforeRank2.getBrandId(),beforeRank2.getCategoryId(),beforeRank2.getProductId(),beforeRank2.getPrice());

        ProductRank beforeRank3 = productRankService.getRanks(RankKey.BRAND_SET_LOWEST,1).get(0);
        log.info("beforeBrandSetLowestRank brandId={},categoryId={},productId={},price={}",beforeRank3.getBrandId(),beforeRank3.getCategoryId(),beforeRank3.getProductId(),beforeRank3.getPrice());




        // when 랭크 삭제 및 갱신
        productRankService.deleteAndRefreshRanksByProductId(p.getId(),brandId,categoryCount);


        // then 검증
        // 1순위 랭크가 내가 아니어야 함.
        ProductRank afterRank1 = productRankService.getRanksByCategoryId(RankKey.CATEGORY_LOWEST,category,1).get(0);
        log.info("afterCategoryLowestRanks brandId={},categoryId={},productId={},price={}",afterRank1.getBrandId(),afterRank1.getCategoryId(),afterRank1.getProductId(),afterRank1.getPrice());
        assertThat(beforeRank1).isNotEqualTo(afterRank1);

        ProductRank afterRank2 = productRankService.getRanksByCategoryId(RankKey.BRAND_CATEGORY_LOWEST,category,1).get(0);
        log.info("afterBrandCategoryLowestRank brandId={},categoryId={},productId={},price={}",afterRank2.getBrandId(),afterRank2.getCategoryId(),afterRank2.getProductId(),afterRank2.getPrice());
        assertThat(beforeRank2).isNotEqualTo(afterRank2);

        ProductRank afterRank3 = productRankService.getRanks(RankKey.BRAND_SET_LOWEST,1).get(0);
        log.info("afterBrandSetLowestRank brandId={},categoryId={},productId={},price={}",afterRank3.getBrandId(),afterRank3.getCategoryId(),afterRank3.getProductId(),afterRank3.getPrice());
        assertThat(beforeRank3).isNotEqualTo(afterRank3);


    }


}