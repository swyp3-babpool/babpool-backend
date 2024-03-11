package com.swyp3.babpool.global.common.response;

import com.swyp3.babpool.global.common.response.config.ApiResponseConfigProperties;
import lombok.Getter;
import org.springframework.http.ResponseCookie;

@Getter
public class CookieProvider {

    private static String domain = ApiResponseConfigProperties.getDomain();
    private static Integer refreshTokenExpireDays = Integer.valueOf(ApiResponseConfigProperties.getRefreshTokenExpireDays());

    public static ResponseCookie ofRefreshToken(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .domain(domain)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(60 * 60 * 24 * refreshTokenExpireDays)
                .build();
    }

    public static ResponseCookie ofRefreshToken(String refreshToken, Integer expireDays) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .domain(domain)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(60 * 60 * 24 * expireDays)
                .build();
    }
}
