package com.synerge.order101.settlement.model.dto;

import com.synerge.order101.settlement.model.entity.Settlement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
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

        return SettlementSummaryDto.builder()
                .settlementId(settlement.getSettlementId())
                .settlementNo(settlement.getSettlementNo())
                .supplierName(settlement.getSupplier() != null ? settlement.getSupplier().getSupplierName() : null)
                .storeName(settlement.getStore() != null ? settlement.getStore().getStoreName() : null)
                .createAt(settlement.getCreatedAt())
                .settlementAmount(settlement.getProductsAmount())
                .settlementQty(settlement.getProductsQty())
                .settlementStatus(settlement.getSettlementStatus().name())
                .build();
    }
}
