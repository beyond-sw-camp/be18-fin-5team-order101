package com.synerge.order101.shipment.model.dto.response;

import java.math.BigDecimal;

public interface OrderQtyAgg {
    Long getStoreOrderId();
    BigDecimal getTotalQty();
}
