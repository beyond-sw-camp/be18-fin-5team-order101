package com.synerge.order101.supplier.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierInfoRes {
    private Long supplierId;
    private String supplierCode;
    private String supplierName;
    private String address;
    private String contactName;
    private String contactNumber;
    private LocalDateTime createdAt;
}
