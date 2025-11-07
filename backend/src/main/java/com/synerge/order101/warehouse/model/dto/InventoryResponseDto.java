package com.synerge.order101.warehouse.model.dto;

import com.synerge.order101.warehouse.model.entity.WarehouseInventory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InventoryResponseDto {

    private Long warehouseInventoryId;

    private String productCode;

    private String productCategory;

    private String productName;

    private Integer onHandQty;

    private Integer safetyQty;

    public static InventoryResponseDto fromEntity (WarehouseInventory inventory) {
        return new InventoryResponseDto(
                inventory.getInventoryId(),
                inventory.getProduct().getProductCode(),
                inventory.getProduct().getProductCategory().getCategoryName(),
                inventory.getProduct().getProductName(),
                inventory.getOnHandQuantity(),
                inventory.getSafetyQuantity()
        );
    }
}
