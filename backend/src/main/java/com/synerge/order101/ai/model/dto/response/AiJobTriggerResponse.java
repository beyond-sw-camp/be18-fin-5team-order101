package com.synerge.order101.ai.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AiJobTriggerResponse {
    private String jobType;
    private String status;
    private String message;

}
