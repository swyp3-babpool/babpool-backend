package com.swyp3.babpool.global.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
public class ApiResponseWithCookie<T> {


    private final LocalDateTime timestamp = LocalDateTime.now();
    private int code;
    private HttpStatus status;
    private String message;
    private T data;
    private List<ResponseCookie> cookies;

    public ApiResponseWithCookie(HttpStatus status, String message, T data, List<ResponseCookie> cookies) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
        this.cookies = cookies;
    }

    @Builder
    public static <T> ApiResponseWithCookie<T> ofMultipleCookies(HttpStatus status, String message, T data, Map<String, String> cookiesKeyValue, String clientDomain) {
        List<ResponseCookie> cookies = cookiesKeyValue.entrySet().stream()
                .map(entry -> createCookie(entry.getKey(), entry.getValue(), clientDomain, 24))
                .toList();
        return new ApiResponseWithCookie<>(status, message, data, cookies);
    }

    public static <T> ApiResponseWithCookie<T> of(HttpStatus status, String message, T data, String cookieKey, String cookieValue, String clientDomain, Integer times) {
        return new ApiResponseWithCookie<>(status, message, data, List.of(createCookie(cookieKey, cookieValue, clientDomain, times)));
    }

    @Builder
    public static <T> ApiResponseWithCookie<T> ofRefreshToken(HttpStatus status, String message, T data, String refreshToken, String clientDomain) {
        List<ResponseCookie> cookies = List.of(createCookie("refreshToken", refreshToken, clientDomain, 24*3));
        return new ApiResponseWithCookie<>(status, message, data, cookies);
    }

    private static ResponseCookie createCookie(String key, String value, String domain, Integer times) {
        return ResponseCookie.from(key, value)
                .domain(domain)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(60 * 60 * times)
                .build();
    }

}
