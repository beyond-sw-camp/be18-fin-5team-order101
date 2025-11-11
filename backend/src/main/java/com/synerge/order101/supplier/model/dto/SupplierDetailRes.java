package com.synerge.order101.supplier.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDetailRes {
    private SupplierInfoRes supplier;

    private List<SupplierProductItemRes> products;

    private int page;
    private int numOfRows;
    private int totalCount;
}
