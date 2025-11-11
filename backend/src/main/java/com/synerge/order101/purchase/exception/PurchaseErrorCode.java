package com.synerge.order101.purchase.exception;

import com.synerge.order101.common.exception.errorcode.ErrorCode;
import org.springframework.http.HttpStatus;

public enum PurchaseErrorCode implements ErrorCode {

    PURCHASE_NOT_FOUND(HttpStatus.NOT_FOUND,"PURCHASE_NOT_FOUND", "해당 발주를 찾을 수 없습니다."),
    INVALID_PURCHASE_REQUEST(HttpStatus.BAD_REQUEST,"INVALID_PURCHASE_REQUEST", "유효하지 않은 구매 요청입니다."),
    PURCHASE_CREATION_FAILED(HttpStatus.BAD_REQUEST,"PURCHASE_CREATION_FAILED", "발주 생성에 실패했습니다."),;

    private final HttpStatus status;
    private final String code;
    private final String message;

    PurchaseErrorCode(HttpStatus status,String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return null;
    }

    @Override
    public String getCode() {
        return "";
    }

    @Override
    public String getMessage() {
        return "";
    }

}
