package com.synerge.order101.shipment.event;

import com.synerge.order101.common.enums.ShipmentStatus;
import com.synerge.order101.common.exception.CustomException;
import com.synerge.order101.common.exception.errorcode.CommonErrorCode;
import com.synerge.order101.order.model.repository.StoreOrderDetailRepository;
import com.synerge.order101.order.model.repository.StoreOrderRepository;
import com.synerge.order101.shipment.exception.ShipmentErrorCode;
import com.synerge.order101.shipment.model.entity.Shipment;
import com.synerge.order101.shipment.model.repository.ShipmentRepository;
import com.synerge.order101.store.model.entity.StoreInventory;
import com.synerge.order101.store.model.repository.StoreInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShipmentInTransitListener {

    private final ShipmentRepository shipmentRepository;
    private final StoreOrderRepository storeOrderRepository;
    private final StoreOrderDetailRepository storeOrderDetailRepository;
    private final StoreInventoryRepository storeInventoryRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void applyInTransit(ShipmentInTransitEvent event) {

        try {
            Shipment shipment = shipmentRepository.findById(event.shipmentId())
                    .orElseThrow(() -> new CustomException(CommonErrorCode.INVALID_REQUEST));

            if (Boolean.TRUE.equals(shipment.getInTransitApplied())) {
                log.info("in_transit already applied: {}", shipment.getShipmentId());
                return;
            }
            if (shipment.getShipmentStatus() != ShipmentStatus.IN_TRANSIT) {
                log.info("skip applyInTransit, status != IN_TRANSIT: {}", shipment.getShipmentId());
                return;
            }

            var order = storeOrderRepository.findById(event.storeOrderId())
                    .orElseThrow(() -> new CustomException(CommonErrorCode.INVALID_REQUEST));

            var lines = storeOrderDetailRepository.findByStoreOrder_StoreOrderId(order.getStoreOrderId());
            if (lines.isEmpty()) throw new CustomException(CommonErrorCode.INVALID_REQUEST);

            lines.forEach(d -> {
                StoreInventory inv = storeInventoryRepository
                        .findByStoreAndProduct(order.getStore(), d.getProduct())
                        .orElseGet(() -> StoreInventory.create(order.getStore(), d.getProduct()));

                inv.increaseInTransit(d.getOrderQty().intValue());
                storeInventoryRepository.save(inv);
            });

            shipment.markInTransitApplied();
            shipmentRepository.save(shipment);

            log.info("in_transit applied: shipmentId={}", shipment.getShipmentId());

        } catch (CustomException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new CustomException(ShipmentErrorCode.INVENTORY_UPDATE_FAILED);
        } catch (Exception e) {
            throw new CustomException(ShipmentErrorCode.EVENT_LISTENER_FAILED);
        }
    }
}