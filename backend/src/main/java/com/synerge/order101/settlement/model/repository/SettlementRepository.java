package com.synerge.order101.settlement.model.repository;

import com.synerge.order101.settlement.model.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Integer> {
}
