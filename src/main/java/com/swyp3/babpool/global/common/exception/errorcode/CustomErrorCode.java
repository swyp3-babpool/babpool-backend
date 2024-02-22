package com.swyp3.babpool.global.common.exception.errorcode;

import org.springframework.http.HttpStatus;

public interface CustomErrorCode {

    HttpStatus getHttpStatus();
    String getMessage();
}
