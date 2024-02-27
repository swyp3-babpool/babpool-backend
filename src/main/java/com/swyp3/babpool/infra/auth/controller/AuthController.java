package com.swyp3.babpool.infra.auth.controller;

import com.swyp3.babpool.global.common.response.ApiResponse;
import com.swyp3.babpool.infra.auth.request.LoginRequestDTO;
import com.swyp3.babpool.infra.auth.response.LoginResponseDTO;
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
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign/in")
    public ApiResponse<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequest){
        //TODO: @NotNull에 걸렸을 때 커스텀 에러 필요
        LoginResponseDTO response = authService.kakaoLogin(loginRequest);
        return ApiResponse.ok(response);
    }
}
