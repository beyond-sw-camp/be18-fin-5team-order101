package com.synerge.order101.shipment.model.service;


import com.synerge.order101.common.enums.ShipmentStatus;
import com.synerge.order101.common.exception.CustomException;
import com.synerge.order101.shipment.exception.errorcode.ShipmentErrorCode;
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

        try{
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

            log.info("Shipment Status Update: WAITING→IN_TRANSIT={}, IN_TRANSIT→DELIVERED={}",
                    waitingUpdate, inTransitUpdate);
        } catch (Exception e){
            throw new CustomException(ShipmentErrorCode.SHIPMENT_UPDATE_FAILED);
        }





    }


}
