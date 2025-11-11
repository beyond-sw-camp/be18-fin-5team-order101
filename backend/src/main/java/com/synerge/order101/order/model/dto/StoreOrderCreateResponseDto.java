package com.synerge.order101.order.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StoreOrderCreateResponseDto {
    private Long storeOrderId;
    private String orderNo;
}
