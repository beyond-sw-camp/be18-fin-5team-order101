package com.synerge.order101.order.exception.errorcode;

import com.synerge.order101.common.exception.errorcode.ErrorCode;
import org.springframework.http.HttpStatus;

public enum OrderErrorCode implements ErrorCode {

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND", "해당 주문을 찾을 수 없습니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND", "해당 가맹점을 찾을 수 없습니다."),
    WAREHOUSE_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND", "해당 창고를 찾을 수 없습니다."),

    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "INVALID_ORDER_STATUS", "유효하지 않은 주문 상태입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    private OrderErrorCode(HttpStatus status,String code, String message) {
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
