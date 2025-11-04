package com.synerge.order101.shipment.scheduler;

import com.synerge.order101.shipment.model.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableScheduling
public class ShipmentScheduler {
    private final ShipmentService shipmentService;

    @Scheduled(cron = "0 * * * * *")
    public void run(){
        log.info("배송 상태 업데이트 스케줄러 시작");
        shipmentService.updateShipmentStatus();
        log.info("배송 상태 업데이트 스케줄러 종료");
    }
}
