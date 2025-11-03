package com.synerge.order101.warehouse.model.repository;


import com.synerge.order101.warehouse.model.entity.WarehouseInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseInventoryRepository extends JpaRepository<WarehouseInventory,Long> {
}
