package com.synerge.order101.order.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StoreOrderSummaryResponseDto {

    Long storeOrderId;

    String orderNo;

    String storeName;

    // 품목 수
    Integer itemCount;

    // 총 수량
    Integer totalQTY;

    LocalDateTime orderDate;

    String orderStatus;

}
