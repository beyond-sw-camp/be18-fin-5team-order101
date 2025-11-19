package com.synerge.order101.ai.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class SmartOrderDetailDto {
    private Long supplierId;
    private String supplierName;
    private LocalDate targetWeek;
    private String requesterName;
    private List<SmartOrderLineItemDto> items;
}
