package com.synerge.order101.shipment.event;

public record ShipmentDeliveredEvent(
        Long shipmentId,
        Long storeOrderId,
        Long storeId) {
}
