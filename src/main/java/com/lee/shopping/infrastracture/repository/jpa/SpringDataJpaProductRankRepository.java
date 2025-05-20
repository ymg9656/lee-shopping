package com.lee.shopping.infrastracture.repository.jpa;

import com.lee.shopping.domain.ProductRank;
import com.lee.shopping.domain.RankKey;
import com.lee.shopping.domain.mapper.ProductRankMapper;
import com.lee.shopping.domain.repository.ProductRankRepository;
import com.lee.shopping.infrastracture.repository.jpa.entity.ProductRankEntity;
import org.hibernate.query.SortDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class SpringDataJpaProductRankRepository implements ProductRankRepository {

    private final JpaProductRankRepository jpaProductRankRepository;

    @Autowired
    public SpringDataJpaProductRankRepository(final JpaProductRankRepository jpaProductRankRepository) {
        this.jpaProductRankRepository = jpaProductRankRepository;
    }

    @Override
    public List<ProductRank> findAllByRankKeyAndCategoryIdAndRankNoLessThanEqual(String rankKey,String categoryId,Integer rankNo) {

        return ProductRankMapper.INSTANCE.fromProducts(jpaProductRankRepository.findAllByRankKeyAndCategoryIdAndRankNoLessThanEqual(rankKey,categoryId,rankNo));
    }

    @Override
    public List<ProductRank> findAllByRankKeyAndCategoryIdAndRankNoLessThanEqualRankOrderDesc(String rankKey, String categoryId, Integer rankNo) {
        return ProductRankMapper.INSTANCE.fromProducts(jpaProductRankRepository.findAllByRankKeyAndCategoryIdAndRankNoLessThanEqualRankOrderDesc(rankKey,categoryId,rankNo));
    }

    @Override
    public List<ProductRank> findAllByRankKeyAndRankNoLessThanEqual(String rankKey, Integer rankNo) {
        return ProductRankMapper.INSTANCE.fromProducts(jpaProductRankRepository.findAllByRankKeyAndRankNoLessThanEqual(rankKey,rankNo));
    }

    @Override
    public List<ProductRank> findAllBrandSetLowestCalculate() {

        //계산할때는 BRAND_CATEGORY_LOWEST 해당키로 조회하여 계산하고
        List<ProductRankEntity> brandSetLowestCal= jpaProductRankRepository.findAllBrandSetLowestCalculate(RankKey.BRAND_CATEGORY_LOWEST.name());

        //계산된 이후에는 BRAND_SET_LOWEST 해당 키로 셋팅 해준다.
        return ProductRankMapper.INSTANCE.toList(brandSetLowestCal,RankKey.BRAND_SET_LOWEST);
    }

    @Override
    public ProductRank save(ProductRank productRank) {
        return null;
    }

    @Override
    public List<ProductRank> saveAll(List<ProductRank> productRanks) {
        List<ProductRankEntity> entities = ProductRankMapper.INSTANCE.toEntities(productRanks);
        return ProductRankMapper.INSTANCE.fromProducts(jpaProductRankRepository.saveAll(entities));
    }
}
