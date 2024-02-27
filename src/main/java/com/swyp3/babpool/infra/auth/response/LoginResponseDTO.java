package com.swyp3.babpool.infra.auth.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {
    private String userUuid;
    private String accessToken;
    private Boolean isRegistered;
}
