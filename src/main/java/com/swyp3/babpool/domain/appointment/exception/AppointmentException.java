package com.swyp3.babpool.domain.appointment.exception;

import com.swyp3.babpool.domain.appointment.exception.eoorcode.AppointmentErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AppointmentException extends RuntimeException{

    private final AppointmentErrorCode errorCode;
    private final String message;
}
