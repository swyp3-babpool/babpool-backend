package com.swyp3.babpool.infra.auth.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {
    private Long userId;
    private String accessToken;
    private Boolean isRegistered;
}
