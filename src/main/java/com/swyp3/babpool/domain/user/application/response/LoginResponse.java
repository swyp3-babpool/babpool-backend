package com.swyp3.babpool.domain.user.application.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String userUuid;
    private String userGrade;
    private String accessToken;
    private Boolean isRegistered;
}
