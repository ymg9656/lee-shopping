package com.lee.shopping.domain;

//설정 정보가 동적으로 관리가 필요한 경우
//properties, db 등등 다른 방식으로 변경 필요
public enum RankKey {

    //각 랭킹별 1등만 가지고 있으면 되지만
    //동시성 이슈 및 집계 관리 차원에서 버퍼를 두고 관리를 한다.
    CATEGORY_LOWEST(5),//카테고리별  최저가
    CATEGORY_HIGHEST(5),//카테고리별  최고가
    BRAND_CATEGORY_LOWEST(5),//브랜드별 카테고리 최저가
    BRAND_SET_LOWEST(5);//브랜드별 모든 카테고리 상품합 최저가

    private final int maxRankSize;

    RankKey(int maxRankSize) {
        this.maxRankSize = maxRankSize;
    }

    public int getMaxRankSize() {
        return maxRankSize;
    }
}
