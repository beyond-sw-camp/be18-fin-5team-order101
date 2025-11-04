package com.synerge.order101.product.model.repository;

import com.synerge.order101.product.model.entity.ProductSupplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSupplierRepository extends JpaRepository<ProductSupplier, Long> {
}
