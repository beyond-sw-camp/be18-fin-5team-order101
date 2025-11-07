package com.synerge.order101.shipment.event;

public record ShipmentInTransitEvent(
        Long shipmentId,
        Long storeOrderId,
        Long storeId
) {
}
