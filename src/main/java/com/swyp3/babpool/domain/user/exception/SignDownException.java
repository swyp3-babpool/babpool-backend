package com.swyp3.babpool.domain.user.exception;

import com.swyp3.babpool.domain.user.exception.errorcode.SignDownExceptionErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignDownException extends RuntimeException{

    private final SignDownExceptionErrorCode signDownExceptionErrorCode;
    private final String message;
}
