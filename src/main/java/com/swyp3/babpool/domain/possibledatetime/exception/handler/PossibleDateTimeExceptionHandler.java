package com.swyp3.babpool.domain.possibledatetime.exception.handler;

import com.swyp3.babpool.domain.possibledatetime.exception.PossibleDateTimeException;
import com.swyp3.babpool.global.common.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class PossibleDateTimeExceptionHandler {

    @ExceptionHandler
    protected ResponseEntity<ApiErrorResponse> handlePossibleDateTimeException(PossibleDateTimeException exception){
        log.error("PossibleDateTimeException getPossibleDateTimeErrorCode() >> "+exception.getErrorCode());
        log.error("PossibleDateTimeException getMessage() >> "+exception.getMessage());
        return ResponseEntity.status(exception.getErrorCode().getHttpStatus())
                .body(ApiErrorResponse.of(exception.getErrorCode()));
    }
}
