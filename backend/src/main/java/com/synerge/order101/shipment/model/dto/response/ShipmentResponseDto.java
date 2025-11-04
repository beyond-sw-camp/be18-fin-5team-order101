package com.synerge.order101.shipment.model.dto.response;

import com.synerge.order101.common.enums.ShipmentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ShipmentResponseDto {
    Long storeOrderId;
    String orderNo;
    String storeName;
    String warehouseName;
    BigDecimal totalQty;
    ShipmentStatus status;
    LocalDateTime requestTime;
}
