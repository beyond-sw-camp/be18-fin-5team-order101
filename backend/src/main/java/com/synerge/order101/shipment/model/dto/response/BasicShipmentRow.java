package com.synerge.order101.shipment.model.dto.response;

import com.synerge.order101.common.enums.ShipmentStatus;

import java.time.LocalDateTime;

public interface BasicShipmentRow {
    Long getStoreOrderId();
    String getOrderNo();
    String getStoreName();
    String getWarehouseName();
    ShipmentStatus getShipmentStatus();
    LocalDateTime getRequestTime();
}
