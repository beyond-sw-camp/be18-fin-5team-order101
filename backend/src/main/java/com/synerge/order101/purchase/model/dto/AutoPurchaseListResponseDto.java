package com.synerge.order101.purchase.model.dto;

import com.synerge.order101.common.enums.OrderStatus;
import com.synerge.order101.purchase.model.entity.PurchaseDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class AutoPurchaseListResponseDto {

    private Long purchaseId;

    private String poNo;

    private String supplierName;

    private Integer itemQty;

    private Integer totalAmount;

    private LocalDateTime requestedAt;

    private OrderStatus status;
}
