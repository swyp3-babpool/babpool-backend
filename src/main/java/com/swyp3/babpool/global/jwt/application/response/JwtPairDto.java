package com.swyp3.babpool.global.jwt.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class JwtPairDto {

    /**
     * @apiNote
     * accessToken : 사용자의 인증을 확인하는데 사용되는 토큰으로, 클라이언트의 Local Storage 에 저장한다.
     * refreshToken : accessToken의 만료시간이 지나면 사용자의 인증을 연장하는데 사용되는 토큰으로, httpsOnly 속성을 가진 쿠키에 저장한다.
     */
    public String accessToken;
    public String refreshToken;

    @Builder
    public JwtPairDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
