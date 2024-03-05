package com.swyp3.babpool.domain.user.exception.handler;

import com.swyp3.babpool.domain.user.exception.SignUpException;
import com.swyp3.babpool.global.common.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SignUpExceptionHandler {
    @ExceptionHandler
    protected ApiErrorResponse handleSignUpException(SignUpException exception){
        log.error("AuthException getSignUpExceptionErrorCode() >> "+exception.getSignUpExceptionErrorCode());
        log.error("AuthException getMessage() >> "+exception.getMessage());
        return ApiErrorResponse.of(exception.getSignUpExceptionErrorCode());
    }
}
