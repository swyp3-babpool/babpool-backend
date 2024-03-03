package com.swyp3.babpool.infra.auth.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthMemberResponse {
    @NotNull(message="platformId는 필수입니다.")
    private String platformId;
    @NotNull(message="프로필 이름은 필수입니다.")
    private String nickname;
    @NotNull(message="프로필 이미지는 필수입니다.")
    private String profile_image;
    @NotNull(message="이메일은 필수입니다.")
    private String email;

}
