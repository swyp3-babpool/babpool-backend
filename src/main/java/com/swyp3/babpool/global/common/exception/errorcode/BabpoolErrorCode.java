package com.swyp3.babpool.global.common.exception.errorcode;

import org.springframework.http.HttpStatus;

public enum BabpoolErrorCode implements CustomErrorCode{

    BABPOOL_ERROR_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "Babpool Error Code"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "올바르지 않은 요청 내용입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    BabpoolErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
