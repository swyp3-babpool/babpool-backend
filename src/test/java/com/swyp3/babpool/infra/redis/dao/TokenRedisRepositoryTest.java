package com.swyp3.babpool.infra.redis.dao;

import com.swyp3.babpool.infra.redis.EmbeddedLocalRedisConfig;
import com.swyp3.babpool.infra.redis.RedisRepositoryConfig;
import com.swyp3.babpool.infra.redis.domain.TokenForRedis;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Import({EmbeddedLocalRedisConfig.class, RedisRepositoryConfig.class})
@DataRedisTest
@ActiveProfiles("test")
class TokenRedisRepositoryTest {

    @Autowired
    private TokenRedisRepository tokenRedisRepository;

    @DisplayName("토큰 저장이 정상적으로 동작하는지 확인한다.")
    @Test
    void save() {
        // given
        String refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIwMTIzNDU2Nzg5Iiwicm9sZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE1MTYyMzkwMjJ9.tac5NQLH2MmB-CmUvYllf2ftt2VnxTGRPxd2fHDopyY";
        TokenForRedis target = TokenForRedis.builder()
                .userId(1000000000000000001L)
                .refreshToken(refreshToken)
                .refreshExpire(10)
                .build();
        // when
        TokenForRedis savedToken = tokenRedisRepository.save(target);

        // then
        tokenRedisRepository.findById(refreshToken)
                .ifPresent(token -> {
                    assertThat(target.getUserId()).isEqualTo(savedToken.getUserId());
                    assertThat(target.getRefreshToken()).isEqualTo(savedToken.getRefreshToken());
                    assertThat(target.getRefreshExpire()).isEqualTo(savedToken.getRefreshExpire());
                });
    }
}