package com.swyp3.babpool.global.common.exception.errorcode;

import org.springframework.http.HttpStatus;

public enum BabpoolErrorCode implements CustomErrorCode{

    BABPOOL_ERROR_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "Babpool Error Code"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    BabpoolErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
