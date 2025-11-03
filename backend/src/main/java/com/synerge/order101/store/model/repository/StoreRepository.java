package com.synerge.order101.store.model.repository;

import com.synerge.order101.store.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StoreRepository extends JpaRepository<Store,Long> {
}
