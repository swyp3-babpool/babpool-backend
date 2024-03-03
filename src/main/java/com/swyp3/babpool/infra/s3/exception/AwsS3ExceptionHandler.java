package com.swyp3.babpool.infra.s3.exception;

import com.swyp3.babpool.global.common.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AwsS3ExceptionHandler {

    @ExceptionHandler(AwsS3Exception.class)
    protected ApiErrorResponse handleAwsS3Exception(AwsS3Exception exception){
        log.error("AwsS3Exception getErrorCode() >> "+exception.getErrorCode());
        log.error("AwsS3Exception getMessage() >> "+exception.getMessage());
        return ApiErrorResponse.of(exception.getErrorCode());
    }
}
