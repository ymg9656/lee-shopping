package com.lee.shopping.infrastracture.repository.jpa;

import com.lee.shopping.domain.Brand;
import com.lee.shopping.domain.Category;
import com.lee.shopping.domain.repository.BrandRepository;
import com.lee.shopping.domain.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class SpringDataJpaBrandRepository implements BrandRepository {

    private final JpaBrandRepository jpaBrandRepository;

    @Autowired
    public SpringDataJpaBrandRepository(final JpaBrandRepository jpaBrandRepository) {
        this.jpaBrandRepository = jpaBrandRepository;
    }

    @Override
    public Optional<Brand> findById(String id) {
        return Optional.empty();
    }

}
