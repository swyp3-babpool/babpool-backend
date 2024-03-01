package com.swyp3.babpool.infra.auth.exception.handler;

import com.swyp3.babpool.global.common.response.ApiErrorResponse;
import com.swyp3.babpool.infra.auth.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler
    protected ApiErrorResponse handleAuthException(AuthException exception){
        log.error("AuthException getAuthExceptionErrorCode() >> "+exception.getAuthExceptionErrorCode());
        log.error("AuthException getMessage() >> "+exception.getMessage());
        return ApiErrorResponse.of(exception.getAuthExceptionErrorCode());
    }
}
