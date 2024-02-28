package com.swyp3.babpool.infra.auth.request;

import com.swyp3.babpool.infra.auth.AuthPlatform;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginRequestDTO {
    @NotNull(message = "요청 정보가 유효하지 않습니다.")
    private AuthPlatform authPlatform;
    @NotNull(message = "요청 정보가 유효하지 않습니다.")
    private String idToken;
}
