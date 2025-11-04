package com.synerge.order101.supplier.model.repository;

import com.synerge.order101.supplier.model.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    int countSupplierByAddress(String address);

    Page<Supplier> findByAddressContainingIgnoreCase(String address, Pageable pageable);
}
