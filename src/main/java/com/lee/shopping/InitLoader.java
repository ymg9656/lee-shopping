package com.lee.shopping;

import com.lee.shopping.domain.service.ProductRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InitLoader implements ApplicationRunner {
    private final ProductRankService productRankService;

    @Override
    public void run(ApplicationArguments args) {
        //TODO
        // 별도 시스템으로 초기화 필요
        // SpringBatch, DB Agent.. 등등
        productRankService.init();
    }
}


