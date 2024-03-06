package com.swyp3.babpool.domain.user.api.requset;

import com.swyp3.babpool.infra.auth.AuthPlatform;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginRequestDTO {
    @NotNull(message = "authPlatform 값은 필수입니다.")
    private AuthPlatform authPlatform;
    @NotNull(message = "code 값은 필수입니다.")
    private String code;
}
