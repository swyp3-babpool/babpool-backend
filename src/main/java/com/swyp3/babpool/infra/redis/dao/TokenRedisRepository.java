package com.swyp3.babpool.infra.redis.dao;

import com.swyp3.babpool.infra.redis.domain.TokenForRedis;
import org.springframework.data.repository.CrudRepository;

public interface TokenRedisRepository extends CrudRepository<TokenForRedis, String> {
}
