package com.synerge.order101.product.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventorySummaryRes {
    private Long productId;
    private String productCode;
    private String productName;
    private long currentQty;
    private long safetyQty;
}
