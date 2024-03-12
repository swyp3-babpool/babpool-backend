package com.swyp3.babpool.global.common.exception.handler;

import com.swyp3.babpool.global.common.exception.errorcode.BabpoolErrorCode;
import com.swyp3.babpool.global.common.response.ApiErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiErrorResponse handleConstraintViolationException(ConstraintViolationException e, WebRequest request) {
        String requestUri = request.getDescription(true).replace("uri=", "");
        String violations = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath().toString() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));

        log.info("ConstraintViolationException occurred at API: {}, Violations: {}", requestUri, violations);

        return ApiErrorResponse.of(HttpStatus.BAD_REQUEST,"Validation error: " + violations);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
        // true: include clientInfo, false: exclude clientInfo
        String requestUri = request.getDescription(true).replace("uri=", "");
        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.info("MethodArgumentNotValidException occurred at API: {}, Errors: {}", requestUri, errors);

        return ApiErrorResponse.of(HttpStatus.BAD_REQUEST, "Validation error: " + errors);
    }
}