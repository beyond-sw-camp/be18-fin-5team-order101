package com.synerge.order101.ai.model.dto.response;

import lombok.Getter;

@Getter
public class SmartOrderLineItemDto {
    private Long smartOrderId;

    private Long productId;
    private String productCode;
    private String productName;

    private Integer forecastQty;
    private Integer recommendedOrderQty;

    private boolean manualEdited;
}
