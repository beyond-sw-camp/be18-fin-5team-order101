package com.synerge.order101.settlement.model.service;

import com.synerge.order101.settlement.model.dto.SettlementSearchCondition;
import com.synerge.order101.settlement.model.dto.SettlementSummaryDto;
import com.synerge.order101.settlement.model.entity.Settlement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SettlementService {
    public Page<SettlementSummaryDto> getSettlements(SettlementSearchCondition cond, Pageable pageable);
}
