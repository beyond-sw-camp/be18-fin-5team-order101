package com.synerge.order101.supplier.exception;

import com.synerge.order101.common.exception.errorcode.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SupplierErrorCode implements ErrorCode {
    SUPPLIER_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "Supplier not found");





    private final HttpStatus status;
    private final String code;
    private final String message;

    SupplierErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
