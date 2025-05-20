package com.lee.shopping.infrastracture.repository.jpa;

import com.lee.shopping.infrastracture.repository.jpa.entity.CategoryEntity;
import com.lee.shopping.infrastracture.repository.jpa.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, String> {




}
