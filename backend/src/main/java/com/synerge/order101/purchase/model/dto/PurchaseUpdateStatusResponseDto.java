package com.synerge.order101.purchase.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PurchaseUpdateStatusResponseDto {

    private Long purchaseId;
    private String poNo;
    private String status;

}
