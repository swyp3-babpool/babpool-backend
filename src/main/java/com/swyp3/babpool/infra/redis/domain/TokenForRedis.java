package com.swyp3.babpool.infra.redis.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@ToString
@Getter
@RedisHash("token")
public class TokenForRedis {

    @Id
    String refreshToken;
    Long userId;

    @TimeToLive(unit = TimeUnit.DAYS)
    Integer refreshExpire;

    @Builder
    public TokenForRedis(Long userId, String refreshToken, Integer refreshExpire) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.refreshExpire = refreshExpire;
    }
}
