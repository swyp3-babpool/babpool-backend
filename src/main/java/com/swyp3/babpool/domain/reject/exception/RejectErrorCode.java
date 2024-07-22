package com.swyp3.babpool.domain.reject.exception;

import com.swyp3.babpool.global.common.exception.errorcode.CustomErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RejectErrorCode implements CustomErrorCode {
    REJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "약속 거절 정보를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
