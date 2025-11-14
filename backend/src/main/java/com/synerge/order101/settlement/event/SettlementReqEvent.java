package com.synerge.order101.settlement.event;

import com.synerge.order101.common.enums.SettlementType;
import com.synerge.order101.order.model.entity.StoreOrder;
import com.synerge.order101.purchase.model.entity.Purchase;
import com.synerge.order101.settlement.model.repository.SettlementRepository;
import com.synerge.order101.store.model.entity.Store;
import com.synerge.order101.supplier.model.entity.Supplier;

import java.math.BigDecimal;

public interface SettlementReqEvent {

    boolean existSettlement(SettlementRepository settlementRepository);

    Purchase purchase();
    Supplier supplier();
    Store store();
    StoreOrder storeOrder();
    SettlementType settlementType();
    BigDecimal settlementAmount();
    Integer settlementQty();


}
