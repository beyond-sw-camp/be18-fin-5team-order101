package com.synerge.order101.shipment.exception.errorcode;

import com.synerge.order101.common.exception.errorcode.ErrorCode;
import org.springframework.http.HttpStatus;

public enum ShipmentErrorCode implements ErrorCode {
    SHIPMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "SHIPMENT_NOT_FOUND", "해당 배송 정보를 찾을 수 없습니다."),
    INVALID_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "INVALID_STATUS_TRANSITION", "잘못된 상태 변경 요청입니다."),
    SHIPMENT_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "SHIPMENT_UPDATE_FAILED", "배송 상태 업데이트 중 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ShipmentErrorCode(HttpStatus status, String code, String message) {
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
