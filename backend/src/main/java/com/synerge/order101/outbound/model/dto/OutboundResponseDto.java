package com.synerge.order101.outbound.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OutboundResponseDto {
    private Long outboundId;

    private String outboundNo;

    private LocalDateTime outboundDatetime;

    private String customerName; // supplierName -> customerName

    private Integer itemCount;

    private Integer totalShippedQty; // totalReceivedQty -> totalShippedQty
}
