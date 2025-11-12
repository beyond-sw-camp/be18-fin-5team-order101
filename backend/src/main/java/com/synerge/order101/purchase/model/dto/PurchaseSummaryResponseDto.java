package com.synerge.order101.purchase.model.dto;


import com.synerge.order101.common.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PurchaseSummaryResponseDto {

    private Long purchaseId;

    private String supplierName;

    private String requesterName;

    private String poNo;

    private Integer totalQty;

    private Integer totalAmount;

    private OrderStatus status;

    private LocalDateTime requestedAt;

}
