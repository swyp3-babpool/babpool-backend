package com.swyp3.babpool.global.util.jwt.application;


import com.swyp3.babpool.global.jwt.JwtTokenizer;
import com.swyp3.babpool.global.jwt.application.JwtServiceImpl;
import com.swyp3.babpool.global.jwt.application.response.JwtPairDto;
import com.swyp3.babpool.infra.redis.EmbeddedLocalRedisConfig;
import com.swyp3.babpool.infra.redis.RedisRepositoryConfig;
import com.swyp3.babpool.infra.redis.dao.TokenRedisRepository;
import com.swyp3.babpool.infra.redis.domain.TokenForRedis;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(classes = {JwtServiceImpl.class, TokenRedisRepository.class, JwtTokenizer.class, EmbeddedLocalRedisConfig.class, RedisRepositoryConfig.class})
class JwtServiceImplTest {

    @Autowired
    JwtServiceImpl jwtService;
    @Autowired
    TokenRedisRepository tokenRepository;

    @DisplayName("jwt token 쌍이 저장된 jwtPairDto 가 반환된다")
    @Test
    void createJwtPair() {
        // given
        Long userId = 100000000000000001L;
        List<String> roles = List.of("ROLE_USER");
        // when
        JwtPairDto jwtPairDto = jwtService.createJwtPair(userId, roles);
        // then
        assertNotNull(jwtPairDto);
        log.info("jwtPairDto : {}", jwtPairDto);
        tokenRepository.findById(String.valueOf(userId)).ifPresent(tokenForRedis -> {
            log.info("tokenForRedis : {}", tokenForRedis);
            assertEquals(jwtPairDto.getRefreshToken(), tokenForRedis.getRefreshToken());
        });
    }

    @DisplayName("refreshToken 으로 accessToken 을 갱신한다")
    @Test
    void extendLoginState() {
        // given
        Long userId = 100000000000000001L;
//        String uuid = "0123456789";
        List<String> roles = List.of("ROLE_USER");
        Integer refreshExpire = 7;

        JwtTokenizer jwtTokenizer = jwtTokenizer
                = new JwtTokenizer("12345678901234567890123456789012","12345678901234567890123456789012");
        String refreshToken = jwtTokenizer.createRefreshToken(userId, roles);

        tokenRepository.save(TokenForRedis.builder()
                .refreshToken(refreshToken)
                .refreshExpire(refreshExpire)
                .userId(userId)
                .build());
        // when
        String accessToken = jwtService.extendLoginState(refreshToken);
        // then
        assertNotNull(accessToken);
        assertThat(jwtTokenizer.parseRefreshToken(refreshToken).getSubject())
                .isEqualTo(jwtTokenizer.parseAccessToken(accessToken).getSubject());
    }

    @DisplayName("refreshToken 으로 로그아웃 처리하면, Redis 에서도 삭제된다.")
    @Test
    void logout() {
        // given
        Long userId = 100000000000000001L;
//        String uuid = "0123456789";
        List<String> roles = List.of("ROLE_USER");
        Integer refreshExpire = 7;

        JwtTokenizer jwtTokenizer = jwtTokenizer
                = new JwtTokenizer("12345678901234567890123456789012","12345678901234567890123456789012");
        String refreshToken = jwtTokenizer.createRefreshToken(userId, roles);

        tokenRepository.save(TokenForRedis.builder()
                .refreshToken(refreshToken)
                .refreshExpire(refreshExpire)
                .userId(userId)
                .build());
        // when
        jwtService.logout(refreshToken);
        // then
        assertThat(tokenRepository.findById(refreshToken)).isEmpty();
    }
}