package com.synerge.order101.supplier.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierProductItemRes {
    private Long productId;
    private String productCode;
    private String supplierProductCode;
    private String productName;
    private BigDecimal price;
    private Integer leadTimeDays;
}
