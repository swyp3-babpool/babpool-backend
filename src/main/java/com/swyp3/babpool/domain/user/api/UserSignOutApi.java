package com.swyp3.babpool.domain.user.api;

import com.swyp3.babpool.global.common.response.ApiResponseWithCookie;
import com.swyp3.babpool.global.jwt.application.JwtServiceImpl;
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
public class UserSignOutApi {

    private final JwtServiceImpl jwtService;

    @PostMapping("/api/user/sign/out")
    public ApiResponseWithCookie signOut(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            Optional<Cookie> optionalCookie = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("refreshToken"))
                    .findAny();
            optionalCookie.ifPresent(cookie -> jwtService.logout(cookie.getValue()));
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
