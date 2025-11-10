package com.synerge.order101.product.exception;

import com.synerge.order101.common.exception.errorcode.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProductErrorCode implements ErrorCode {
    INVALID_SMALL_CATEGORY(HttpStatus.NOT_FOUND, "NOT_FOUND", "Invalid small category id"),
    MUST_BE_SMALL(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "categorySmallId must be 소 level"),
    SMALL_MEDIUM(HttpStatus.NOT_ACCEPTABLE, "NOT_ACCEPTABLE", "중분류가 소분류의 부모와 일치하지 않습니다."),
    LARGE_MEDIUM(HttpStatus.NOT_ACCEPTABLE, "NOT_ACCEPTABLE", "대분류가 중분류의 부모와 일치하지 않습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "Product not found");





    private final HttpStatus status;
    private final String code;
    private final String message;

    ProductErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
