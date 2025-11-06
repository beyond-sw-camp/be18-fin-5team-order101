package com.synerge.order101.order.model.repository;

import com.synerge.order101.order.model.entity.StoreOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreOrderDetailRepository extends JpaRepository<StoreOrderDetail,Long> {
    List<StoreOrderDetail> findByStoreOrder_StoreOrderId(Long storeOrderId);
}
