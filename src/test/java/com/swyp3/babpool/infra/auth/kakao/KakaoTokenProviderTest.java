package com.swyp3.babpool.infra.auth.kakao;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class KakaoTokenProviderTest {

    private String redirectUri = "https://bab-pool.com/auth/kakao/callback";

    @Test
    void resolveKakaoRedirectUriWhenRequestFromLocalHost() {
        // given
        String localhostFlag = "true";

        // when
        reAssignKakaoRedirectUriWhenRequestFromLocalHost(localhostFlag);

        // then
        assertEquals("http://localhost:5173/auth/kakao/callback", redirectUri);
    }

    @Test
    void resolveKakaoRedirectUriWhenRequestFromNotLocalHost() {
        // given
        String localhostFlag = null;

        // when
        reAssignKakaoRedirectUriWhenRequestFromLocalHost(localhostFlag);

        // then
        assertEquals("https://bab-pool.com/auth/kakao/callback", redirectUri);
    }

    private void reAssignKakaoRedirectUriWhenRequestFromLocalHost(String localhostFlag) {
        redirectUri = localhostFlag != null && localhostFlag.equals("true") ? "http://localhost:5173/auth/kakao/callback" : redirectUri;
    }

}