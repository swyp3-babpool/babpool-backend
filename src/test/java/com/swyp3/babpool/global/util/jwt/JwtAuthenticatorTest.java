package com.swyp3.babpool.global.util.jwt;

import com.swyp3.babpool.global.jwt.JwtAuthenticator;
import com.swyp3.babpool.global.jwt.JwtTokenizer;
import com.swyp3.babpool.global.uuid.dao.UserUuidRepository;
import com.swyp3.babpool.global.uuid.exception.UuidException;
import com.swyp3.babpool.global.uuid.util.UuidResolver;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@MybatisTest
class JwtAuthenticatorTest {

    private JwtAuthenticator jwtAuthenticator;
    private JwtTokenizer jwtTokenizer;
    private UuidResolver uuidResolver;
    @Autowired
    UserUuidRepository userUuidRepository;

    @BeforeEach
    void setUp() {
        jwtTokenizer = new JwtTokenizer("12345678901234567890123456789012", "12345678901234567890123456789012");
        uuidResolver = new UuidResolver();
        jwtAuthenticator = new JwtAuthenticator(jwtTokenizer, uuidResolver, userUuidRepository);
    }

    @DisplayName("토큰을 파싱해 클레임을 반환한다. 올바른 토큰인 경우")
    @Test
    void authenticate_proper_access_token() {
        // given
        Long userId = 100000000000000001L;
//        String userUUID = "46707d92-02f4-4817-8116-a4c3b23e6266";
        List<String> roles = List.of("ROLE_USER");
        String accessToken = jwtTokenizer.createAccessToken(userId, roles);
        log.info("accessToken: {}", accessToken);
        // when
        Claims claims = jwtAuthenticator.authenticate(accessToken);
        log.info("claims.getSubject(): {}", claims.getSubject());
        // then
        assertThat(claims).isNotEmpty();
        assertThat(claims.getSubject()).isEqualTo(String.valueOf(userId));
    }

    @DisplayName("토큰을 파싱해 클레임을 반환한다. 올바르지 않은 토큰인 경우")
    @Test
    void authenticate_improper_access_token() {
        // given
        Long userId = 100000000000000001L;
//        String userUUID = "46707d92-02f4-4817-8116-a4c3b23e6266";
        List<String> roles = List.of("ROLE_USER");
        String accessToken = jwtTokenizer.createAccessToken(userId, roles);
        // when
        // then
        assertThrows(SignatureException.class, () -> {
            jwtAuthenticator.authenticate(accessToken + "a");
        });
    }

    @DisplayName("토큰을 파싱해 클레임을 반환한다. 만료된 토큰인 경우")
    @Test
    void authenticate_expired_access_token() {
        // given
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0NjcwN2Q5Mi0wMmY0LTQ4MTctODExNi1hNGMzYjIzZTYyNjYiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzA4OTA5NDQ4LCJleHAiOjF9.YX_hZKjbewwz1FFaj6aOLPogUs1jPK5UC1aR1dHbB7A";
        // when
        // then
        assertThrows(ExpiredJwtException.class, () -> {
            jwtAuthenticator.authenticate(accessToken);
        });
    }

    @DisplayName("토큰을 파싱해 클레임을 반환한다. 토큰이 빈 문자열인 경우")
    @Test
    void authenticate_empty_access_token() {
        // given
        String accessToken = "";
        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> {
            jwtAuthenticator.authenticate(accessToken);
        });
    }

    @DisplayName("토큰을 파싱해 클레임을 반환한다. 지원하지 않는 알고리즘의 토큰인 경우")
    @Test
    void authenticate_unsupported_access_token() {
        // given
        String accessToken = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiI0NjcwN2Q5Mi0wMmY0LTQ4MTctODExNi1hNGMzYjIzZTYyNjYiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzA4OTA5NDQ4LCJleHAiOjF9.MroKm4-jOBCvpVZayg2duSri8ZdLSc5au1N3Mhpgbr0Q_B_3D088pk1rCDkOKDfr";
        // when
        // then
        assertThrows(InvalidKeyException.class, () -> {
            jwtAuthenticator.authenticate(accessToken);
        });
    }

//    @DisplayName("userUuid 으로 UserUuidRepository 에서 userId를 조회한다. 존재하지 않는 경우 예외가 발생한다.")
//    @Test
//    void findUserIdByUserUuid_not_exist() {
//        // given
//        String userUUID = "46707d92-02f4-4817-8116-a4c3b23e6266";
//        // when
//        // then
//        assertThrows(UuidException.class, () -> {
//            jwtAuthenticator.jwtTokenUserIdResolver(userUUID);
//        });
//    }
}