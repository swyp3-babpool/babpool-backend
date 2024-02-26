package com.swyp3.babpool.global.uuid.exception;

import com.swyp3.babpool.global.common.response.ApiErrorResponse;
import com.swyp3.babpool.global.util.jwt.exception.BabpoolJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UuidExceptionHandler {

    @ExceptionHandler
    protected ApiErrorResponse handleUuidException(UuidException exception){
        log.error("UuidException getUuidErrorCode() >> "+exception.getErrorCode());
        log.error("UuidException getMessage() >> "+exception.getMessage());
        return ApiErrorResponse.of(exception.getErrorCode());
    }

}
