package com.swyp3.babpool.global.util.jwt;


import com.swyp3.babpool.global.jwt.JwtTokenizer;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class JwtTokenizerTest {

    JwtTokenizer jwtTokenizer;

    @BeforeEach
    void setUp(){
        jwtTokenizer = new JwtTokenizer("12345678901234567890123456789012", "12345678901234567890123456789012");
    }

    @DisplayName("UUID와 Role 정보를 이용하여 AccessToken 생성한다.")
    @Test
    void createAccessToken() {
        // given
        String userUUID = "46707d92-02f4-4817-8116-a4c3b23e6266";
        List<String> roles = List.of("ROLE_USER");
        // when
        String accessToken = jwtTokenizer.createAccessToken(userUUID, roles);
        // then
        assertNotNull(accessToken);
        log.info("accessToken: {}", accessToken);
        Claims claims = jwtTokenizer.parseAccessToken(accessToken);
        log.info("claims: {}", claims);
        Assertions.assertThat(claims.getSubject()).isEqualTo(userUUID);
    }

    @DisplayName("UUID와 Role 정보를 이용하여 RefreshToken 생성한다.")
    @Test
    void createRefreshToken() {
        // given
        String userUUID = "46707d92-02f4-4817-8116-a4c3b23e6267";
        List<String> roles = List.of("ROLE_USER");
        // when
        String refreshToken = jwtTokenizer.createRefreshToken(userUUID, roles);
        // then
        assertNotNull(refreshToken);
        log.info("refreshToken: {}", refreshToken);
        Claims claims = jwtTokenizer.parseAccessToken(refreshToken);
        log.info("claims: {}", claims);
        Assertions.assertThat(claims.getSubject()).isEqualTo(userUUID);
    }

    @DisplayName("AccessToken을 파싱하여 Claims 정보를 추출한다.")
    @Test
    void parseAccessToken() {
        // given
        String userUUID = "46707d92-02f4-4817-8116-a4c3b23e6268";
        List<String> roles = List.of("ROLE_USER");
        String accessToken = jwtTokenizer.createAccessToken(userUUID, roles);
        // when
        Claims claims = jwtTokenizer.parseAccessToken(accessToken);
        // then
        assertNotNull(claims);
        log.info("claims: {}", claims);
        Assertions.assertThat(claims.getSubject()).isEqualTo(userUUID);
    }

    @DisplayName("RefreshToken을 파싱하여 Claims 정보를 추출한다.")
    @Test
    void parseRefreshToken() {
        // given
        String userUUID = "46707d92-02f4-4817-8116-a4c3b23e6269";
        List<String> roles = List.of("ROLE_USER");
        String refreshToken = jwtTokenizer.createRefreshToken(userUUID, roles);
        // when
        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);
        // then
        assertNotNull(claims);
        log.info("claims: {}", claims);
        Assertions.assertThat(claims.getSubject()).isEqualTo(userUUID);
    }


}