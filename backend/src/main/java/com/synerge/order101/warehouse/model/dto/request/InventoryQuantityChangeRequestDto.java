package com.synerge.order101.warehouse.model.dto.request;

import lombok.Getter;

@Getter
public class InventoryQuantityChangeRequestDto {
//    private Long warehouseId;
    private Long productId;
    private Integer quantity;
}

