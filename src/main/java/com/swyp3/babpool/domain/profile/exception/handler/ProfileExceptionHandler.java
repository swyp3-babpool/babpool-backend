package com.swyp3.babpool.domain.profile.exception.handler;

import com.swyp3.babpool.domain.profile.exception.ProfileException;
import com.swyp3.babpool.global.common.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ProfileExceptionHandler {

    @ExceptionHandler
    protected ApiErrorResponse handleProfileException(ProfileException exception){
        log.error("ProfileException getProfileErrorCode() >> "+exception.getErrorCode());
        log.error("ProfileException getMessage() >> "+exception.getMessage());
        return ApiErrorResponse.of(exception.getErrorCode());
    }
}
