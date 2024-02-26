package com.swyp3.babpool.global.util.jwt.exception.handler;

import com.swyp3.babpool.global.common.response.ApiErrorResponse;
import com.swyp3.babpool.global.util.jwt.exception.BabpoolJwtException;
import com.swyp3.babpool.global.util.jwt.exception.BadCredentialsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class JwtExceptionHandler {

    @ExceptionHandler
    protected ApiErrorResponse handleBabpoolJwtException(BabpoolJwtException exception){
        log.error("JwtException getJwtExceptionErrorCode() >> "+exception.getJwtExceptionErrorCode());
        log.error("JwtException getMessage() >> "+exception.getMessage());
        return ApiErrorResponse.of(exception.getJwtExceptionErrorCode());
    }

    @ExceptionHandler
    protected ApiErrorResponse handleJwtBadCredentialsException(BadCredentialsException exception){
        log.error("BadCredentialsException getJwtExceptionErrorCode() >> "+exception.getJwtExceptionErrorCode());
        log.error("BadCredentialsException getMessage() >> "+exception.getMessage());
        return ApiErrorResponse.of(exception.getJwtExceptionErrorCode());
    }
}
