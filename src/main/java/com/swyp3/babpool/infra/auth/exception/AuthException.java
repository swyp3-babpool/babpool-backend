package com.swyp3.babpool.infra.auth.exception;

import com.swyp3.babpool.infra.auth.exception.errorcode.AuthExceptionErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthException extends RuntimeException{
    private final AuthExceptionErrorCode authExceptionErrorCode;
    private String message;
}
