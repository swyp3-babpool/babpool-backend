package com.swyp3.babpool.global.uuid.exception;

import com.swyp3.babpool.global.common.exception.errorcode.CustomErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UuidErrorCode implements CustomErrorCode {
    NOT_FOUND_USER_UUID(HttpStatus.NOT_FOUND, "Not found user uuid."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
