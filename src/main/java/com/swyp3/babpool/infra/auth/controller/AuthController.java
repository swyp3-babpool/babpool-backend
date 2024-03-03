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
import org.springframework.http.HttpStatus;
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
    public ApiResponseWithCookie<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequest){
        LoginResponseWithRefreshToken loginResponseData = authService.kakaoLogin(loginRequest);
        Boolean isRegistered = loginResponseData.getLoginResponseDTO().getIsRegistered();

        //로그인 성공한 경우
        if(isRegistered)
            return ApiResponseWithCookie.ofRefreshToken(HttpStatus.OK,"로그인에 성공하였습니다",
                        loginResponseData.getLoginResponseDTO(), loginResponseData.getRefreshToken());
        //추가정보 입력이 필요한 경우
        return ApiResponseWithCookie.ofRefreshToken(HttpStatus.UNAUTHORIZED,"추가정보 입력이 필요한 사용자입니다",
                        loginResponseData.getLoginResponseDTO(), loginResponseData.getRefreshToken());
    }
}