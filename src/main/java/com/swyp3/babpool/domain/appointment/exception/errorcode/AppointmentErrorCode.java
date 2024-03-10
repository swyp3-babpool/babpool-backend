package com.swyp3.babpool.domain.appointment.exception.errorcode;

import com.swyp3.babpool.global.common.exception.errorcode.CustomErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AppointmentErrorCode implements CustomErrorCode {

    DUPLICATE_APPOINTMENT_REQUEST(HttpStatus.BAD_REQUEST, "이미 예약된 시간대가 존재합니다."),
    APPOINTMENT_SEND_NOT_FOUND(HttpStatus.NOT_FOUND, "발신한 밥약이 존재하지 않습니다."),
    APPOINTMENT_RECEIVE_NOT_FOUND(HttpStatus.NOT_FOUND, "수신한 밥약이 존재하지 않습니다."),
    APPOINTMENT_DONE_NOT_FOUND(HttpStatus.NOT_FOUND, "완료한 밥약이 존재하지 않습니다."),
    APPOINTMENT_REFUSE_NOT_FOUND(HttpStatus.NOT_FOUND, "거절된 밥약이 존재하지 않습니다."),
    APPOINTMENT_POSSIBLE_DATETIME_NOT_FOUND(HttpStatus.NOT_FOUND, "밥약 가능 시간이 존재하지 않습니다."),
    APPOINTMENT_IS_NOT_WAITING(HttpStatus.BAD_REQUEST, "대기 중인 밥약 요청이 아닙니다."),
    APPOINTMENT_NOT_RECEIVER(HttpStatus.BAD_REQUEST,"밥약 요청을 거절할 권한이 없습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
