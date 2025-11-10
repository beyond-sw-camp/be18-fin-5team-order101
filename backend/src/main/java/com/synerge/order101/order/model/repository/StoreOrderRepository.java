package com.synerge.order101.order.model.repository;

import com.synerge.order101.order.model.entity.StoreOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreOrderRepository extends JpaRepository<StoreOrder, Long> {

    // 주문 + 매장 + 상세 + 상품을 한 번에 패치해서 N+1과 LazyInitialization 문제를 회피
    @Query("select so from StoreOrder so " +
            "left join fetch so.store s " +
            "left join fetch so.storeOrderDetails d " +
            "left join fetch d.product p " +
            "where so.storeOrderId = :id")
    Optional<StoreOrder> findWithDetailsById(@Param("id") Long id);
}
