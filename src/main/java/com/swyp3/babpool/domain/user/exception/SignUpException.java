package com.swyp3.babpool.domain.user.exception;

import com.swyp3.babpool.domain.user.exception.errorcode.SignUpExceptionErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignUpException extends RuntimeException{
    private final SignUpExceptionErrorCode signUpExceptionErrorCode;
    private final String message;
}
