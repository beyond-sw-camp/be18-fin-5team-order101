package com.synerge.order101.shipment.scheduler;

import com.synerge.order101.shipment.model.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class ShipmentScheduler {
    private final ShipmentService shipmentService;

    @Scheduled(cron = "0 * * * * *")
    public void run(){
        shipmentService.updateShipmentStatus();
    }
}
