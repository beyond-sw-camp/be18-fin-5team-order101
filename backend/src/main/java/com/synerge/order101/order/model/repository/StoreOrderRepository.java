package com.synerge.order101.order.model.repository;

import com.synerge.order101.common.enums.OrderStatus;
import com.synerge.order101.order.model.entity.StoreOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreOrderRepository extends JpaRepository<StoreOrder, Long> {

    Page<StoreOrder> findByOrderStatus(OrderStatus status, Pageable pageable);

    @Query("select so from StoreOrder so")
    Page<StoreOrder> findOrderAllStatus(Pageable pageable);
}
