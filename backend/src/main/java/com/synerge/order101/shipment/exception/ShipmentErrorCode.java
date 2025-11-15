package com.synerge.order101.shipment.exception;

import com.synerge.order101.common.exception.errorcode.ErrorCode;
import org.springframework.http.HttpStatus;

public enum ShipmentErrorCode implements ErrorCode {
    SHIPMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"SHIPMENT_NOT_FOUND", "해당 배송 정보를 찾을 수 없습니다."),
    SHIPMENT_QUERY_FAILED(HttpStatus.UNPROCESSABLE_ENTITY, "SHIPMENT_QUERY_FAILED", "배송 데이터 조회 중 오류가 발생했습니다."),
    SHIPMENT_NOT_DELIVERED(HttpStatus.CONFLICT, "SHIPMENT_NOT_DELIVERED", "배송 완료 상태에서만 재고 적재 처리가 가능합니다."),
    INVENTORY_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "INVENTORY_UPDATE_FAILED", "가맹점 재고 반영 중 오류가 발생했습니다."),
    EVENT_LISTENER_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "EVENT_LISTENER_FAILED", "이벤트 리스너 처리 중 오류가 발생했습니다.");

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
