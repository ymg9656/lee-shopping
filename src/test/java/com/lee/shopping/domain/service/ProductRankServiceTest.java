package com.lee.shopping.domain.service;

import com.lee.shopping.domain.RankKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductRankServiceTest {
    @Autowired
    private ProductRankService productRankService;

    @Test
    void test() {
       //같은 브랜드의 같은 카테고리의 상품을 계속해서 등록할때 랭킹 업데이트가 되는지?
        int rankMaxSize = RankKey.CATEGORY_LOWEST.getMaxRankSize();
        String brand="A";
        String category = "상의";
        for(int i =0; i < rankMaxSize*2; i++){

        }



    }
}