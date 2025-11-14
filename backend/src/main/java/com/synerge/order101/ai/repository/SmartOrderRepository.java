package com.synerge.order101.ai.repository;

import com.synerge.order101.ai.model.entity.DemandForecast;
import com.synerge.order101.ai.model.entity.SmartOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SmartOrderRepository extends JpaRepository<SmartOrder,Long> {
    //내림차순
    List<SmartOrder> findByStoreIdOrderByTargetWeek(Long storeId);

    List<SmartOrder> findByStoreIdAndSmartOrderStatus(Long storeId, SmartOrder.SmartOrderStatus status);

    List<SmartOrder> findByTargetWeek(LocalDate targetWeek);
}
