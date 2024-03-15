package com.swyp3.babpool.infra.auth.dao;

import com.swyp3.babpool.infra.auth.AuthPlatform;
import com.swyp3.babpool.infra.auth.domain.Auth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface AuthRepository {
    void save(Auth oauth);

    Optional<Auth> findByUserId(Long userId);

    int updateOauthPlatformId(@Param("userId") Long userId);
}
