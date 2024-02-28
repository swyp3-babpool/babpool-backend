package com.swyp3.babpool.infra.auth.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthMemberResponse {
    @NotNull
    private String platformId;
    @NotNull
    private String nickname;
    @NotNull
    private String profile_image;
    @NotNull
    private String email;

}
