package com.synerge.order101.shipment.event;

import com.synerge.order101.common.enums.ShipmentStatus;
import com.synerge.order101.common.exception.CustomException;
import com.synerge.order101.common.exception.errorcode.CommonErrorCode;
import com.synerge.order101.order.model.entity.StoreOrder;
import com.synerge.order101.order.model.entity.StoreOrderDetail;
import com.synerge.order101.order.model.repository.StoreOrderDetailRepository;
import com.synerge.order101.order.model.repository.StoreOrderRepository;
import com.synerge.order101.shipment.exception.errorcode.ShipmentErrorCode;
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

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShipmentInventoryListener {

    private final ShipmentRepository shipmentRepository;
    private final StoreOrderRepository storeOrderRepository;
    private final StoreOrderDetailRepository storeOrderDetailRepository;
    private final StoreInventoryRepository storeInventoryRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void applyInventory(ShipmentDeliveredEvent event) {
        try {
            Shipment shipment = shipmentRepository.findById(event.shipmentId())
                    .orElseThrow(() -> new CustomException(CommonErrorCode.INVALID_REQUEST));

            if (Boolean.TRUE.equals(shipment.getInventoryApplied())) {
                log.info("already applied (delivered): shipmentId={}", event.shipmentId());
                return;
            }
            if (shipment.getShipmentStatus() != ShipmentStatus.DELIVERED) {
                throw new CustomException(ShipmentErrorCode.SHIPMENT_NOT_DELIVERED);
            }

            StoreOrder order = storeOrderRepository.findById(event.storeOrderId())
                    .orElseThrow(() -> new CustomException(CommonErrorCode.INVALID_REQUEST));

            List<StoreOrderDetail> lines = storeOrderDetailRepository
                    .findByStoreOrder_StoreOrderId(order.getStoreOrderId());
            if (lines.isEmpty()) throw new CustomException(CommonErrorCode.INVALID_REQUEST);

            boolean hadTransitPhase = Boolean.TRUE.equals(shipment.getInTransitApplied());

            lines.forEach(d -> {
                StoreInventory inv = storeInventoryRepository
                        .findByStoreAndProduct(order.getStore(), d.getProduct())
                        .orElseGet(() -> StoreInventory.create(order.getStore(), d.getProduct()));

                int qty = d.getOrderQty().intValue();


                if (hadTransitPhase) {
                    inv.moveTransitToOnHand(qty);
                } else {
                    inv.increaseOnHand(qty);
                }
                storeInventoryRepository.save(inv);
            });

            shipment.markInventoryApplied();
            shipmentRepository.save(shipment);

            log.info("store inventory applied (delivered): shipmentId={}, storeOrderId={}",
                    event.shipmentId(), event.storeOrderId());

        } catch (CustomException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new CustomException(ShipmentErrorCode.INVENTORY_UPDATE_FAILED);
        } catch (Exception e) {
            throw new CustomException(ShipmentErrorCode.EVENT_LISTENER_FAILED);
        }
    }
}