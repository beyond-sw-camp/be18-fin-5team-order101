package com.synerge.order101.warehouse.model.repository;


import com.synerge.order101.warehouse.model.entity.WarehouseInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseInventoryRepository extends JpaRepository<WarehouseInventory,Long> {
}
