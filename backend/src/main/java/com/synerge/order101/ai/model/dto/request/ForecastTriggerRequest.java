package com.synerge.order101.ai.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ForecastTriggerRequest {
    private String targetWeek;
}
