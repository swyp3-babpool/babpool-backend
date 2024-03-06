package com.swyp3.babpool.domain.appointment.exception.eoorcode;

import com.swyp3.babpool.global.common.exception.errorcode.CustomErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AppointmentErrorCode implements CustomErrorCode {

    DUPLICATE_APPOINTMENT_REQUEST(HttpStatus.BAD_REQUEST, "이미 예약된 시간대가 존재합니다.")

    ;
    private final HttpStatus httpStatus;
    private final String message;
}
