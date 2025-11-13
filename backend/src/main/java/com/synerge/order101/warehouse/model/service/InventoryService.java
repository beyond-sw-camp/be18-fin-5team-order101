package com.synerge.order101.warehouse.model.service;

import com.synerge.order101.order.model.entity.StoreOrder;
import com.synerge.order101.purchase.model.entity.Purchase;
import com.synerge.order101.warehouse.model.dto.response.InventoryResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface InventoryService {
    List<InventoryResponseDto> getInventoryList();

    void decreaseInventory(Long productId, int quantity);

//    void increaseInventory(Long productId, int quantity);
    void increaseInventory(Purchase purchase);

    void updateDailySafetyStock();

    void triggerAutoPurchase();
}
