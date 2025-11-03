package com.synerge.order101.warehouse.model.repository;


import com.synerge.order101.warehouse.model.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse,Long> {
}
