package com.swyp3.babpool.domain.user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExitInfoRepository {
    void saveExitInfo(@Param("userId") Long userId, @Param("exitReason") String exitReason);
}
