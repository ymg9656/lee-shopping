package com.lee.shopping.infrastracture.repository.jpa;

import com.lee.shopping.domain.Brand;
import com.lee.shopping.domain.Category;
import com.lee.shopping.domain.mapper.BrandMapper;
import com.lee.shopping.domain.mapper.CategoryMapper;
import com.lee.shopping.domain.repository.BrandRepository;
import com.lee.shopping.domain.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class SpringDataJpaBrandRepository implements BrandRepository {

    private final JpaBrandRepository jpaBrandRepository;

    @Autowired
    public SpringDataJpaBrandRepository(final JpaBrandRepository jpaBrandRepository) {
        this.jpaBrandRepository = jpaBrandRepository;
    }

    @Override
    public Brand save(Brand brand) {
        return BrandMapper.INSTANCE.to(jpaBrandRepository.save(BrandMapper.INSTANCE.fromBrand(brand)));
    }

    @Override
    public Optional<Brand> findById(String id) {
        return jpaBrandRepository.findById(id).map(BrandMapper.INSTANCE::to);
    }

    @Override
    public List<Brand> findAll() {
        return BrandMapper.INSTANCE.toList(jpaBrandRepository.findAll());
    }

    @Override
    public void deleteById(String id) {
        jpaBrandRepository.deleteById(id);
    }

}
