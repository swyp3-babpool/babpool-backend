package com.swyp3.babpool.domain.user.api;

import com.swyp3.babpool.global.common.response.ApiResponse;
import com.swyp3.babpool.global.common.response.CookieProvider;
import com.swyp3.babpool.global.jwt.JwtAuthenticator;
import com.swyp3.babpool.global.jwt.application.JwtServiceImpl;
import com.swyp3.babpool.infra.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserSignOutApi {

    private final AuthService authService;
    private final JwtServiceImpl jwtService;
    private final JwtAuthenticator jwtAuthenticator;

    @PostMapping("/api/user/sign/out")
    public ResponseEntity<ApiResponse<String>> signOut(@CookieValue(value = "refreshToken", required = false) String refreshTokenFromCookie){
        log.info("/api/user/sign/out sign out request start");
        if(StringUtils.hasText(refreshTokenFromCookie)){
            log.info("/api/user/sign/out refreshTokenFromCookie : " + refreshTokenFromCookie);
            Long userId = jwtAuthenticator.jwtRefreshTokenToUserIdResolver(refreshTokenFromCookie);
            authService.socialServiceSignOut(userId, authService.getAuthPlatformByUserId(userId));
            jwtService.logout(refreshTokenFromCookie);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, CookieProvider.ofRefreshToken("", 0).toString())
                .body(ApiResponse.ok("sign out success"));

    }

}
