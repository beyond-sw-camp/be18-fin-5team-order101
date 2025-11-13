package com.synerge.order101.settlement.model.repository;

import com.synerge.order101.settlement.model.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long> , SettlementRepositoryCustom{


}
