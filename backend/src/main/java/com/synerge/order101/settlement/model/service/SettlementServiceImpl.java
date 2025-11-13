package com.synerge.order101.settlement.model.service;

import com.synerge.order101.settlement.model.dto.SettlementSearchCondition;
import com.synerge.order101.settlement.model.dto.SettlementSummaryDto;
import com.synerge.order101.settlement.model.entity.Settlement;
import com.synerge.order101.settlement.model.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettlementServiceImpl implements SettlementService{

    private final SettlementRepository settlementRepository;

    @Override
    public Page<SettlementSummaryDto> getSettlements(SettlementSearchCondition cond, Pageable pageable) {

        Page<Settlement> page = settlementRepository.search(cond, pageable);

        return page.map(SettlementSummaryDto::fromEntity);

    }

    private SettlementSummaryDto getSettlementSummaryDto(Settlement settlement){
        return SettlementSummaryDto.fromEntity(settlement);
    }
}
