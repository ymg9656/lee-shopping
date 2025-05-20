package com.lee.shopping.infrastracture.repository.jpa;

import com.lee.shopping.infrastracture.repository.jpa.entity.BrandEntity;
import com.lee.shopping.infrastracture.repository.jpa.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaBrandRepository extends JpaRepository<BrandEntity, String> {




}
