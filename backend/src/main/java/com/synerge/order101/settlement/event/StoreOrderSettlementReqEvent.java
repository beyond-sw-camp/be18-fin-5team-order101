package com.synerge.order101.settlement.event;

import com.synerge.order101.common.enums.SettlementType;
import com.synerge.order101.order.model.entity.StoreOrder;
import com.synerge.order101.purchase.model.entity.Purchase;
import com.synerge.order101.settlement.model.repository.SettlementRepository;
import com.synerge.order101.store.model.entity.Store;
import com.synerge.order101.supplier.model.entity.Supplier;

import java.math.BigDecimal;

public record StoreOrderSettlementReqEvent(StoreOrder storeOrder) implements SettlementReqEvent {

    @Override
    public BigDecimal settlementAmount() {
        return storeOrder.getStoreOrderDetails().stream()
                .map(detail -> detail.getAmount()
                        .multiply(BigDecimal.valueOf(detail.getOrderQty())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Integer settlementQty() {

        return storeOrder.getStoreOrderDetails().stream()
                .mapToInt(detail -> detail.getOrderQty() != null ? detail.getOrderQty() : 0)
                .sum();
    }

    @Override
    public boolean existSettlement(SettlementRepository settlementRepository) {
        return settlementRepository.existsByStoreOrder_StoreOrderId(storeOrder.getStoreOrderId());
    }

    @Override
    public Purchase purchase() {
        return null;
    }

    @Override
    public Supplier supplier() {
        return null;
    }

    @Override
    public Store store() {
        return storeOrder.getStore();
    }

    @Override
    public StoreOrder storeOrder() {
        return storeOrder;
    }

    @Override
    public SettlementType settlementType() {
        return SettlementType.AR;
    }


}
