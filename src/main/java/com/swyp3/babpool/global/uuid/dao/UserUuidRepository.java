package com.swyp3.babpool.global.uuid.dao;

import com.swyp3.babpool.global.uuid.domain.UserUuid;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserUuidRepository {

    Optional<UserUuid> findByUserUuId(String uuid);
}
