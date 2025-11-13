package com.synerge.order101.purchase.model.dto;

import com.synerge.order101.order.model.dto.StoreOrderCreateRequest;
import com.synerge.order101.purchase.model.entity.Purchase;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseCreateRequest {

    @NonNull
    Long supplierId;

    @NonNull
    Long userId;

    @NonNull
    Long warehouseId;

    @NonNull
    Purchase.OrderType orderType;

    @NonNull
    LocalDateTime deadline;

    List<Item> items = List.of();

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        @NonNull
        private Long productId;

        @NonNull
        private Integer orderQty;
    }
}
