package com.synerge.order101.shipment.model.service;

import com.synerge.order101.common.enums.ShipmentStatus;
import com.synerge.order101.shipment.model.dto.response.ShipmentResponseDto;
import com.synerge.order101.shipment.model.repository.ShipmentListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShipmentListService {

    private final ShipmentListRepository shipmentListRepository;

    @Transactional(readOnly = true)
    public Page<ShipmentResponseDto> findShipments(
            String orderNo,
            Long storeId,
            ShipmentStatus status,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    ) {
        return shipmentListRepository.findPage(orderNo, storeId, status, from, to, pageable);
    }
}