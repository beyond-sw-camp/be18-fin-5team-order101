package com.synerge.order101.order.model.repository;

import com.synerge.order101.order.model.entity.StoreOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreOrderRepository extends JpaRepository<StoreOrder, Long>{


}
