package com.swyp3.babpool.infra.auth.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class LoginResponseWithRefreshToken {
    LoginResponseDTO loginResponseDTO;
    String refreshToken;
}
