package com.swyp3.babpool.global.jwt.api;

import com.swyp3.babpool.global.common.response.ApiResponse;
import com.swyp3.babpool.global.jwt.application.JwtService;
import com.swyp3.babpool.global.jwt.exception.BabpoolJwtException;
import com.swyp3.babpool.global.jwt.exception.errorcode.JwtExceptionErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class JwtApiPermitted {

    private final JwtService jwtService;

    @PostMapping("/api/token/access/refresh")
    public ApiResponse<String> refreshAccessToken(HttpServletRequest request){
        // if request.getCookies() is null, throw exception
        if(request.getCookies() == null || request.getCookies().length == 0){
            throw new BabpoolJwtException(JwtExceptionErrorCode.NOT_FOUND_TOKEN, "RefreshToken 이 존재하지 않습니다.");
        }
        Optional<Cookie> refreshTokenCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst();
        String refreshToken = refreshTokenCookie.map(Cookie::getValue).orElseThrow(
                () -> new BabpoolJwtException(JwtExceptionErrorCode.NOT_FOUND_TOKEN, "RefreshToken 이 존재하지 않습니다."));
        return ApiResponse.of(HttpStatus.OK, "로그인 연장 성공", jwtService.extendLoginState(refreshToken));
    }

}
