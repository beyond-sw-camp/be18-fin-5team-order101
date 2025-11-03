package com.synerge.order101.store.model.repository;

import com.synerge.order101.store.model.entity.StoreInventory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StoreInventoryRepository extends JpaRepository<StoreInventory,Long> {
}
