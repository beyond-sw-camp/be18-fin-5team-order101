package com.synerge.order101.product.model.repository;

import com.synerge.order101.product.model.entity.CategoryLevel;
import com.synerge.order101.product.model.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Long> {

    List<ProductCategory> findByCategoryLevelAndParentIsNull(CategoryLevel level);

    List<ProductCategory> findByParent_ProductCategoryId(Long parentId);
}
