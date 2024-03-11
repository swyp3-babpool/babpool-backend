package com.swyp3.babpool.domain.user.exception.errorcode;

import com.swyp3.babpool.global.common.exception.errorcode.CustomErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SignDownExceptionErrorCode implements CustomErrorCode {
    FAILED_TO_UPDATE_USER_STATE(HttpStatus.INTERNAL_SERVER_ERROR,"회원탈퇴에 실패하였습니다.")
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
