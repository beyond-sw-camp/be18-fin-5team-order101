package com.synerge.order101.ai.model.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiMetricResponseDto {
    private Double mae;
    private Double mape;
    private Double smape;
}
