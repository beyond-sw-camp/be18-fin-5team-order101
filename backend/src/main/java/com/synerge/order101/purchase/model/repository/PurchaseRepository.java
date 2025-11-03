package com.synerge.order101.purchase.model.repository;

import com.synerge.order101.purchase.model.entity.PurchaseDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<PurchaseDetail, Long> {
}
