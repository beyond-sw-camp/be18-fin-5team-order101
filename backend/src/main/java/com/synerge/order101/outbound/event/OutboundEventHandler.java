package com.synerge.order101.outbound.event;

import com.synerge.order101.outbound.model.service.OutboundService;
import com.synerge.order101.shipment.event.ShipmentInTransitEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboundEventHandler {

    private final OutboundService outboundService;

    @EventListener
    public void handleShipmentInTransit(ShipmentInTransitEvent event) {
        outboundService.createOutboundFromShipment(event);
    }
}

