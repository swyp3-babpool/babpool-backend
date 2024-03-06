package com.swyp3.babpool.domain.review.exception;

import com.swyp3.babpool.global.common.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ReviewExceptionHandler {

    @ExceptionHandler
    protected ApiErrorResponse handleReviewException(ReviewException exception){
        log.error("ReviewException getReviewErrorCode() >> "+exception.getErrorCode());
        log.error("ReviewException getMessage() >> "+exception.getMessage());
        return ApiErrorResponse.of(exception.getErrorCode());
    }
}
