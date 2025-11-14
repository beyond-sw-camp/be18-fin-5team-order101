package com.synerge.order101.ai.exception;

import com.synerge.order101.common.exception.errorcode.ErrorCode;
import org.springframework.http.HttpStatus;

public enum AiErrorCode implements ErrorCode {
    FORECAST_NOT_FOUND(HttpStatus.NOT_FOUND,"FORECAST_NOT_FOUND", "수요 예측 정보를 찾을 수 없습니다."),
    SMART_ORDER_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "SMART_ORDER_UPDATE_FAILED", "DRAFT 상태에서만 수정 가능합니다."),
    SMART_ORDER_SUBMIT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "SMART_ORDER_SUBMIT_FAILED", "DRAFT 상태에서만 상신 가능합니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;


    AiErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }


    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
