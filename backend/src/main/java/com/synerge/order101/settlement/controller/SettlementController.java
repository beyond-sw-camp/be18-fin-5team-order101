package com.synerge.order101.settlement.controller;

import com.synerge.order101.settlement.model.dto.SettlementSearchCondition;
import com.synerge.order101.settlement.model.dto.SettlementSummaryDto;
import com.synerge.order101.settlement.model.dto.SettlementDetailResponseDto;
import com.synerge.order101.settlement.model.entity.Settlement;
import com.synerge.order101.settlement.model.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 필수 : 동적 쿼리 작성.
 */
@RestController
@RequestMapping("/api/v1/settlements")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    @GetMapping
    public ResponseEntity<Page<SettlementSummaryDto>> getSettlements(
            @ModelAttribute SettlementSearchCondition cond,
            Pageable pageable
    ) {
        Page<SettlementSummaryDto> results = settlementService.getSettlements(cond, pageable);

        return ResponseEntity.ok(results);
    }

    @GetMapping("{settlementId}")
    public ResponseEntity<SettlementDetailResponseDto> getSettlementDetail(
            @PathVariable("settlementId") String settlementId) {

        return null;
    }

    @PostMapping()
    public ResponseEntity<Void> createSettlement() {

        return null;
    }

    // 정산 발행??
}
