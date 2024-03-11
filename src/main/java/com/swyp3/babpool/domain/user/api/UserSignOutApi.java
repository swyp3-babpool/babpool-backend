package com.swyp3.babpool.domain.user.api;

import com.swyp3.babpool.global.common.response.ApiResponseWithCookie;
import com.swyp3.babpool.global.jwt.JwtAuthenticator;
import com.swyp3.babpool.global.jwt.application.JwtServiceImpl;
import com.swyp3.babpool.infra.auth.service.AuthService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserSignOutApi {

    private final AuthService authService;
    private final JwtServiceImpl jwtService;
    private final JwtAuthenticator jwtAuthenticator;

    @PostMapping("/api/user/sign/out")
    public ApiResponseWithCookie signOut(HttpServletRequest request){
        log.info("sign out request start");
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            Optional<Cookie> optionalCookie = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("refreshToken"))
                    .findAny();
            optionalCookie.ifPresent(cookie -> {
                String refreshTokenFromCookie = cookie.getValue();
                Long userId = jwtAuthenticator.jwtRefreshTokenToUserIdResolver(refreshTokenFromCookie);
                authService.socialServiceSignOut(userId, authService.getAuthPlatformByUserId(userId));
                jwtService.logout(refreshTokenFromCookie);
            });
        }
        return ApiResponseWithCookie.ofCookie(
                HttpStatus.OK,
                "sign out success",
                null,
                "refreshToken",
                "",
                true,
                0);
    }

}
