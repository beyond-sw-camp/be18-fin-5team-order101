package com.synerge.order101.product.model.repository;

import com.synerge.order101.product.model.entity.ProductSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductSupplierRepository extends JpaRepository<ProductSupplier, Long> {

    @Query("""
SELECT ps
FROM ProductSupplier ps
JOIN FETCH ps.product p
WHERE ps.supplier.supplierId = :supplierId
""")
    Page<ProductSupplier> findBySupplierWithProduct(
            @Param("supplierId") Long supplierId,
            Pageable pageable
    );
}
