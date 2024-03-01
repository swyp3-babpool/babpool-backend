package com.swyp3.babpool.global.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
public class ApiResponseWithCookie<T> {

    @Value("${property.cookie.domain}")
    private static String domain;
    @Value("${property.jwt.refreshTokenExpireDays}")
    private static Integer refreshTokenExpireDays;

    private final LocalDateTime timestamp = LocalDateTime.now();
    private int code;
    private HttpStatus status;
    private String message;
    private T data;
    private ResponseCookie cookie;

    public ApiResponseWithCookie(HttpStatus status, String message, T data, ResponseCookie cookie) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
        this.cookie = cookie;
    }

    @Builder
    public static <T> ApiResponseWithCookie<T> ofCookie(HttpStatus status, String message, T data, String key, String value, boolean httpOnlyFlag, Integer expireDays) {
        ResponseCookie cookie = ResponseCookie.from(key, value)
                .domain(domain)
                .httpOnly(httpOnlyFlag)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(60 * 60 * 24 * expireDays)
                .build();
        return new ApiResponseWithCookie<>(status, message, data, cookie);
    }

    @Builder
    public static <T> ApiResponseWithCookie<T> ofRefreshToken(HttpStatus status, String message, T data, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .domain(domain)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(60 * 60 * 24 * refreshTokenExpireDays)
                .build();
        return new ApiResponseWithCookie<>(status, message, data, cookie);
    }

}
