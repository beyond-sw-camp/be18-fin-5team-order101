package com.synerge.order101.settlement.model.dto;

import com.synerge.order101.settlement.model.entity.Settlement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class SettlementSummaryDto {

    Long settlementId;

    String settlementNo;
    String supplierName;
    String storeName;
    LocalDateTime createAt;
    BigDecimal settlementAmount;
    Integer settlementQty;
    String settlementStatus;

    public static SettlementSummaryDto fromEntity(Settlement settlement){

        if(settlement == null) {
            return null;
        }
        // TODO : 금액 및 수량 집계 로직
        return SettlementSummaryDto.builder()
                .settlementId(settlement.getSettlementId())
                .settlementNo(settlement.getSettlementNo())
                .supplierName(settlement.getSupplier().getSupplierName())
                .storeName(settlement.getStore().getStoreName())
                .createAt(settlement.getCreatedAt())
                .settlementAmount(BigDecimal.ZERO)
                .settlementQty(0)
                .settlementStatus(settlement.getSettlementStatus().name())
                .build();
    }
}
