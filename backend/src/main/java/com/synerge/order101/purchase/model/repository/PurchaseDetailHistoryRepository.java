package com.synerge.order101.purchase.model.repository;

import com.synerge.order101.purchase.model.entity.PurchaseDetailHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseDetailHistoryRepository extends JpaRepository<PurchaseDetailHistory, Long> {
}
