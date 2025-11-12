package com.synerge.order101.warehouse.model.repository;


import com.synerge.order101.warehouse.model.entity.WarehouseInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseInventoryRepository extends JpaRepository<WarehouseInventory,Long> {

    @Query("""
        SELECT wi
        FROM WarehouseInventory wi
        JOIN FETCH wi.product p
        JOIN FETCH p.productCategory c
    """)
    List<WarehouseInventory> findAllWithProduct();

}
