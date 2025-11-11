package com.synerge.order101.order.model.repository;

import com.synerge.order101.order.model.entity.StoreOrderDetail;
import com.synerge.order101.order.model.entity.StoreOrderStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreOrderStatusLogRepository extends JpaRepository<StoreOrderStatusLog,Long> {
}
