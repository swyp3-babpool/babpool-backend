package com.swyp3.babpool.infra.redis.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@RedisHash("token")
public class TokenForRedis {

    @Id
    String userUUID;

    String refreshToken;

    @TimeToLive(unit = TimeUnit.DAYS)
    Integer refreshExpire;

    @Builder
    public TokenForRedis(String userUUID, String refreshToken, Integer refreshExpire) {
        this.userUUID = userUUID;
        this.refreshToken = refreshToken;
        this.refreshExpire = refreshExpire;
    }
}
