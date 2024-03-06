package com.swyp3.babpool.domain.user.exception.errorcode;

import com.swyp3.babpool.global.common.exception.errorcode.CustomErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SignUpExceptionErrorCode implements CustomErrorCode {
    IS_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST,"이미 등록된 사용자입니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
