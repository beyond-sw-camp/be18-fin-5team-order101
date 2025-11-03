package com.synerge.order101.supplier.model.repository;

import com.synerge.order101.supplier.model.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
