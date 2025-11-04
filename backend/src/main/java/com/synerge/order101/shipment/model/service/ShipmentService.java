package com.synerge.order101.shipment.model.service;

import com.synerge.order101.common.enums.ShipmentStatus;
import com.synerge.order101.shipment.model.entity.Shipment;
import com.synerge.order101.shipment.model.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


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



    }


}
