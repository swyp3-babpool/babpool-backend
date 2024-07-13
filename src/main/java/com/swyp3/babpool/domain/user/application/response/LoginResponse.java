package com.swyp3.babpool.domain.user.application.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private Long userId;
    private String userGrade;
    private String accessToken;
    private Boolean isRegistered;
}
