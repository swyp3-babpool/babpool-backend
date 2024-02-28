package com.swyp3.babpool.infra.auth.exception.handler;

import com.swyp3.babpool.global.common.response.ApiErrorResponse;
import com.swyp3.babpool.global.util.jwt.exception.BabpoolJwtException;
import com.swyp3.babpool.infra.auth.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler
    protected ApiErrorResponse handleAuthException(AuthException exception){
        log.error("JwtException getJwtExceptionErrorCode() >> "+exception.getAuthExceptionErrorCode());
        log.error("JwtException getMessage() >> "+exception.getMessage());
        return ApiErrorResponse.of(exception.getAuthExceptionErrorCode());
    }
}
