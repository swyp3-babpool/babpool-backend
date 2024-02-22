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
        TokenForRedis tokenForRedis = TokenForRedis.builder()
                .userUUID("0123456789")
                .refreshToken("token-token")
                .refreshExpire(10)
                .build();
        // when
        TokenForRedis savedToken = tokenRedisRepository.save(tokenForRedis);

        // then
        tokenRedisRepository.findById("0123456789")
                .ifPresent(token -> {
                    assertThat(token.getUserUUID()).isEqualTo(savedToken.getUserUUID());
                    assertThat(token.getRefreshToken()).isEqualTo(savedToken.getRefreshToken());
                    assertThat(token.getRefreshExpire()).isEqualTo(savedToken.getRefreshExpire()-1);
                });
    }
}