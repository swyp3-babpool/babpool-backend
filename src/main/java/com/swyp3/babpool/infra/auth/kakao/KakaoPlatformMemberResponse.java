package com.swyp3.babpool.infra.auth.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoPlatformMemberResponse {
    private String platformId;
    private String email;
}
