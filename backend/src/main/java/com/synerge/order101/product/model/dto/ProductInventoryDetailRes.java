package com.synerge.order101.product.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInventoryDetailRes {
    private InventorySummaryRes summary;
    private List<InventoryMovementRes> items;
    private int page;
    private int numOfRows;
    private int totalCount;
}
