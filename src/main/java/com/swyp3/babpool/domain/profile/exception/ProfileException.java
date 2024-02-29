package com.swyp3.babpool.domain.profile.exception;

import com.swyp3.babpool.domain.profile.exception.errorcode.ProfileErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProfileException extends RuntimeException{

    private final ProfileErrorCode errorCode;
    private final String message;
}
