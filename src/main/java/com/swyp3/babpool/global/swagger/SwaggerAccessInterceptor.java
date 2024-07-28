package com.swyp3.babpool.global.swagger;

import com.swyp3.babpool.domain.user.domain.UserRole;
import com.swyp3.babpool.global.jwt.JwtAuthenticator;
import com.swyp3.babpool.global.jwt.exception.BabpoolJwtException;
import com.swyp3.babpool.global.jwt.exception.errorcode.JwtExceptionErrorCode;
import com.swyp3.babpool.global.swagger.exception.SwaggerErrorCode;
import com.swyp3.babpool.global.swagger.exception.SwaggerException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SwaggerAccessInterceptor implements HandlerInterceptor {

    private final JwtAuthenticator jwtAuthenticator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 쿠키가 존재하지 않을 경우
        if(request.getCookies() == null || request.getCookies().length == 0){
            throw new SwaggerException(SwaggerErrorCode.NOTFOUND_COOKIE, "쿠키가 Reqeust Header에 존재하지 않습니다.");
        }

        // refreshToken 쿠키를 찾아서 refreshToken을 가져온다.
        Optional<Cookie> refreshTokenCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst();
        String refreshToken = refreshTokenCookie.map(Cookie::getValue).orElseThrow(
                () -> new SwaggerException(SwaggerErrorCode.NOTFOUND_TOKEN_IN_COOKIE,
                        "쿠키에 refreshToken 이 존재하지 않습니다.")
        );

        Claims authenticate;

        try {
            authenticate = jwtAuthenticator.authenticate(refreshToken);
        }catch (Exception e){
            throw new SwaggerException(SwaggerErrorCode.INVALID_TOKEN, "토큰이 유효하지 않습니다.");
        }

        // 토큰이 유효하지 않을 경우
        if (authenticate == null || authenticate.isEmpty()){
            throw new SwaggerException(SwaggerErrorCode.INVALID_TOKEN_EMPTY, "토큰이 비어 있습니다.");
        }

        // 토큰의 roles가 비어있는 경우
        List<String> roles = authenticate.get("roles", List.class);
        if(roles == null || roles.isEmpty()){
            throw new SwaggerException(SwaggerErrorCode.INVALID_TOKEN_AUTHORITY, "권한을 찾을 수 없습니다.");
        }

        // ADMIN이 아닐 경우
        if (!roles.contains(UserRole.ADMIN.name())){
            throw new SwaggerException(SwaggerErrorCode.INVALID_TOKEN_AUTHORITY, "ADMIN 권한이 없습니다.");
        }

        return true;
    }
}
