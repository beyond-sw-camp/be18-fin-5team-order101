package com.synerge.order101.warehouse.model.repository;


import com.synerge.order101.warehouse.model.entity.WarehouseInventoryLedger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseInventoryLedgerRepository extends JpaRepository<WarehouseInventoryLedger,Long> {
}
