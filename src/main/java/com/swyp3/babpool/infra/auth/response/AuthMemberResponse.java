package com.swyp3.babpool.infra.auth.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthMemberResponse {
    private String platformId;
    private String nickname;
    private String profile_image;
    private String email;

}
