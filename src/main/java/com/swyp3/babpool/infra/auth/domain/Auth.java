package com.swyp3.babpool.infra.auth.domain;

import com.swyp3.babpool.domain.user.domain.User;
import com.swyp3.babpool.domain.user.domain.UserRole;
import com.swyp3.babpool.domain.user.domain.UserStatus;
import com.swyp3.babpool.infra.auth.AuthPlatform;
import com.swyp3.babpool.infra.auth.response.AuthMemberResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Auth {
    private Long oauthId;
    private Long userId;
    private AuthPlatform oauthPlatformName;
    private String oauthPlatformId;

    @Builder
    public Auth(Long oauthId, Long userId, AuthPlatform oauthPlatformName, String oauthPlatformId) {
        this.oauthId = oauthId;
        this.userId = userId;
        this.oauthPlatformName = oauthPlatformName;
        this.oauthPlatformId = oauthPlatformId;
    }
}
