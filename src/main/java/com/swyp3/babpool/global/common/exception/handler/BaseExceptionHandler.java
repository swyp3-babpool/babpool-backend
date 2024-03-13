package com.swyp3.babpool.global.common.exception.handler;

import com.swyp3.babpool.global.common.exception.errorcode.BabpoolErrorCode;
import com.swyp3.babpool.global.common.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@Deprecated
//@Slf4j
//@RestControllerAdvice
//public class BaseExceptionHandler {
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ApiErrorResponse handle_MethodArgumentNotValidException(MethodArgumentNotValidException exception) {
//        log.error("MethodArgumentNotValidException getmessage() >> "+exception.getMessage());
//        return ApiErrorResponse.of(BabpoolErrorCode.INVALID_REQUEST);
//    }
//}
