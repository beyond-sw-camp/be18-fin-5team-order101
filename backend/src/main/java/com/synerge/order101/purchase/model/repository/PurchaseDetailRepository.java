package com.synerge.order101.purchase.model.repository;

import com.synerge.order101.purchase.model.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseDetailRepository extends JpaRepository<Purchase, Long> {
}
