package com.synerge.order101.product.exception;

import com.synerge.order101.common.exception.errorcode.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProductErrorCode implements ErrorCode {
    INVALID_SMALL_CATEGORY(HttpStatus.NOT_FOUND, "NOT_FOUND", "Invalid small category id"),
    INTERNAL_SERVERe_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 내부 에러");





    private final HttpStatus status;
    private final String code;
    private final String message;

    ProductErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
