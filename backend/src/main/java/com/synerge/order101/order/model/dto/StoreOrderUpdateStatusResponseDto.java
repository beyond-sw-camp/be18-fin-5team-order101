package com.synerge.order101.order.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class StoreOrderUpdateStatusResponseDto {

    private Long storeOrderId;
    private String status;
    private LocalDate updatedDateAt;
}
