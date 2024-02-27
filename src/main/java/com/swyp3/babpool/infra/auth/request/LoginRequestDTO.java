package com.swyp3.babpool.infra.auth.request;

import com.swyp3.babpool.infra.auth.AuthPlatform;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginRequestDTO {
    @NotNull
    private AuthPlatform authPlatform;
    @NotNull
    private String idToken;
}
