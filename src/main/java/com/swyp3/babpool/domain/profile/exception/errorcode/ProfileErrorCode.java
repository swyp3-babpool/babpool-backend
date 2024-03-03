package com.swyp3.babpool.domain.profile.exception.errorcode;

import com.swyp3.babpool.global.common.exception.errorcode.CustomErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProfileErrorCode implements CustomErrorCode {

    ;

    private final HttpStatus httpStatus;
    private final String message;
}
