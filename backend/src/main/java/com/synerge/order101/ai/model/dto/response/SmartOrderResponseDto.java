package com.synerge.order101.ai.model.dto.response;

import com.synerge.order101.ai.model.entity.SmartOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@AllArgsConstructor
@Builder
public class SmartOrderResponseDto {
    private Long id;
    private Long storeId;
    private Long productId;
    private Long demandForecastId;
    private Date targetWeek;
    private Integer recommendedOrderQty;
    private Integer forecastQty;
    private SmartOrder.SmartOrderStatus smartOrderStatus;
    private LocalDateTime snapshotAt;
    private LocalDateTime updatedAt;
}
