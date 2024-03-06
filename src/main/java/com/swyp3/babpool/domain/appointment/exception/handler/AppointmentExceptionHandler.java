package com.swyp3.babpool.domain.appointment.exception.handler;

import com.swyp3.babpool.domain.appointment.exception.AppointmentException;
import com.swyp3.babpool.global.common.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AppointmentExceptionHandler {

    @ExceptionHandler
    protected ApiErrorResponse handleAppointmentException(AppointmentException exception){
        log.error("AppointmentException getAppointmentErrorCode() >> "+exception.getErrorCode());
        log.error("AppointmentException getMessage() >> "+exception.getMessage());
        return ApiErrorResponse.of(exception.getErrorCode());
    }
}
