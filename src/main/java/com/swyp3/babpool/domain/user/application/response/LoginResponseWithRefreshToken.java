package com.swyp3.babpool.domain.user.application.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseWithRefreshToken {
    LoginResponse loginResponse;
    String refreshToken;
}
