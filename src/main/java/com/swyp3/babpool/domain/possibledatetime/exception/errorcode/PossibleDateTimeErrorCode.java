package com.swyp3.babpool.domain.possibledatetime.exception.errorcode;

import com.swyp3.babpool.global.common.exception.errorcode.CustomErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PossibleDateTimeErrorCode implements CustomErrorCode {

    POSSIBLE_DATETIME_NOT_FOUND(HttpStatus.NOT_FOUND, "밥약 가능한 일정이 존재하지 않습니다."),
    POSSIBLE_DATETIME_ALREADY_RESERVED(HttpStatus.BAD_REQUEST, "이미 예약된 시간대 입니다. 다른 시간대를 다시 선택해주세요."),
    POSSIBLE_DATETIME_STATUS_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "밥약 가능한 일정 상태 변경에 실패하였습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
