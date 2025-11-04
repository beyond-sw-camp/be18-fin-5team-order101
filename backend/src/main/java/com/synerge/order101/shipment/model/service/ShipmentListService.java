package com.synerge.order101.shipment.model.service;

import com.synerge.order101.common.enums.ShipmentStatus;
import com.synerge.order101.shipment.model.dto.response.BasicShipmentRow;
import com.synerge.order101.shipment.model.dto.response.OrderQtyAgg;
import com.synerge.order101.shipment.model.dto.response.ShipmentResponseDto;
import com.synerge.order101.shipment.model.repository.ShipmentListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        Page<BasicShipmentRow> base = shipmentListRepository.findBasicPage(orderNo, storeId, status, from, to, pageable);

        // 2) 합계 맵 조회
        List<Long> ids = base.stream().map(BasicShipmentRow::getStoreOrderId).toList();
        Map<Long, BigDecimal> qtyMap = ids.isEmpty()
                ? Collections.emptyMap()
                : shipmentListRepository.sumOrderQtyByOrderIds(ids)
                .stream()
                .collect(Collectors.toMap(OrderQtyAgg::getStoreOrderId, OrderQtyAgg::getTotalQty));

        // 3) 합쳐서 DTO로 반환
        List<ShipmentResponseDto> content = base.stream()
                .map(r -> ShipmentResponseDto.builder()
                        .storeOrderId(r.getStoreOrderId())
                        .orderNo(r.getOrderNo())
                        .storeName(r.getStoreName())
                        .warehouseName(r.getWarehouseName())
                        .status(r.getShipmentStatus())
                        .requestTime(r.getRequestTime())
                        .totalQty(qtyMap.getOrDefault(r.getStoreOrderId(), BigDecimal.ZERO))
                        .build())
                .toList();

        return new PageImpl<>(content, pageable, base.getTotalElements());
    }
}
