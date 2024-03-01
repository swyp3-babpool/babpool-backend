package com.swyp3.babpool.infra.auth.controller;

import com.swyp3.babpool.global.common.response.ApiResponse;
import com.swyp3.babpool.global.common.response.ApiResponseWithCookie;
import com.swyp3.babpool.infra.auth.request.LoginRequestDTO;
import com.swyp3.babpool.infra.auth.response.LoginResponseDTO;
import com.swyp3.babpool.infra.auth.response.LoginResponseWithRefreshToken;
import com.swyp3.babpool.infra.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign/in")
    public ApiResponse<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequest){
        LoginResponseWithRefreshToken loginResponseWithRefreshToken = authService.kakaoLogin(loginRequest);
        Boolean isRegistered = loginResponseWithRefreshToken.getLoginResponseDTO().getIsRegistered();
        //추가정보 입력 필요 -> JWT 반환 X
        if(isRegistered==false)
            return ApiResponse.ok(ApiResponseWithCookie.ofRefreshToken(loginResponseWithRefreshToken);
        //로그인 성공 -> JWT 반환
        else
            return ApiResponse.ok(loginResponseWithRefreshToken.getLoginResponseDTO());
    }
}