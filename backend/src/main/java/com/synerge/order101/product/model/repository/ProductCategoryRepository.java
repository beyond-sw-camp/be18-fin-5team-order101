package com.synerge.order101.product.model.repository;

import com.synerge.order101.product.model.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Long> {
}
