package com.synerge.order101.product.model.repository;

import com.synerge.order101.product.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
