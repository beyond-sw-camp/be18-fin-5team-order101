package com.synerge.order101.shipment.model.service;


import com.synerge.order101.common.enums.ShipmentStatus;
import com.synerge.order101.shipment.model.entity.Shipment;
import com.synerge.order101.shipment.model.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShipmentService {
    private final ShipmentRepository shipmentRepository;

    @Transactional
    public void updateShipmentStatus(){
        LocalDateTime now = LocalDateTime.now();

        int waitingUpdate = shipmentRepository.updateStatus(
                ShipmentStatus.WAITING,
                ShipmentStatus.IN_TRANSIT,
                now.minusMinutes(30),
                now
        );

        int inTransitUpdate = shipmentRepository.updateStatus(
                ShipmentStatus.IN_TRANSIT,
                ShipmentStatus.DELIVERED,
                now.minusMinutes(60),
                now
        );

        log.info("배송 상태 업데이트 완료: WAITING→IN_TRANSIT={}건, IN_TRANSIT→DELIVERED={}건",
                waitingUpdate, inTransitUpdate);



    }


}
