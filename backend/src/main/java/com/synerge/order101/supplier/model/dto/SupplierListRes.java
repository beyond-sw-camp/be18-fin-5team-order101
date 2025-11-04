package com.synerge.order101.supplier.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierListRes {
    private Long supplierId;
    private String supplierCode;
    private String supplierName;
    private String address;
    private String contactName;
    private String contactNumber;
    private LocalDateTime createdDate;
}
