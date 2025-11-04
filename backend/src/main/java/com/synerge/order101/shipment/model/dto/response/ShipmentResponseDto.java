package com.synerge.order101.shipment.model.dto.response;

import com.synerge.order101.common.enums.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ShipmentResponseDto {
    private Long storeOrderId;
    private String orderNo;
    private String storeName;
    private BigDecimal totalQty;          // 합계 수량
    private ShipmentStatus shipmentStatus;
    private LocalDateTime orderDatetime;  // 요청 시간
}