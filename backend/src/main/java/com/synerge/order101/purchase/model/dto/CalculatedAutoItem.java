package com.synerge.order101.purchase.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CalculatedAutoItem {
    Long productId;
    int orderQty;
    Long supplierId;
}

