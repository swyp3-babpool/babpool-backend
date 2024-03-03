package com.swyp3.babpool.infra.auth.exception.errorcode;

import com.swyp3.babpool.global.common.exception.errorcode.CustomErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionErrorCode implements CustomErrorCode {
    AUTH_JWT_ERROR(HttpStatus.UNAUTHORIZED,"AUTH JWT에서 오류가 발생했습니다."),
    AUTH_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST,"유효하지 않은 토큰입니다."),
    AUTH_MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED,"올바르지 않은 토큰입니다."),
    AUTH_UNSUPPORTED_ID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED,"OAuth Identity Token의 형식이 올바르지 않습니다."),
    AUTH_PUBLIC_KEY_ERROR(HttpStatus.UNAUTHORIZED,"OAuth Public Key 생성 중 오류가 발생했습니다."),
    AUTH_ERROR_CONNECT_WITH_KAKAO(HttpStatus.INTERNAL_SERVER_ERROR,"Kakao로부터 id Token을 응답받는 과정 중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
