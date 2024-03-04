package com.swyp3.babpool.infra.auth.response;

import com.swyp3.babpool.domain.user.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {
    private String userUuid;
    private String userGrade;
    private String accessToken;
    private Boolean isRegistered;
}
