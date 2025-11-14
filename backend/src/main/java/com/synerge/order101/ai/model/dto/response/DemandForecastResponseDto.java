package com.synerge.order101.ai.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DemandForecastResponseDto {
    private Long id;
    private Long warehouseId;
    private Long storeId;
    private Long productId;
    private LocalDate targetWeek;
    private Integer predictedQty;
    private Integer actualOrderQty;
    private Integer mape;
    private String externalFactorsJson;
    private LocalDateTime snapshotAt;
    private LocalDateTime updatedAt;
}
