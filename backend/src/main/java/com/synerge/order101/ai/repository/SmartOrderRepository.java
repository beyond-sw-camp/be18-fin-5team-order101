package com.synerge.order101.ai.repository;

import com.synerge.order101.ai.model.entity.SmartOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SmartOrderRepository extends JpaRepository<SmartOrder,Long> {
    // 프로퍼티 경로 사용
    List<SmartOrder> findByStore_StoreIdOrderByTargetWeekDesc(Long storeId);

    List<SmartOrder> findByStore_StoreIdAndSmartOrderStatus(Long storeId, SmartOrder.SmartOrderStatus status);

    List<SmartOrder> findByTargetWeek(LocalDate targetWeek);
}

