package com.swyp3.babpool.domain.user.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExitInfoRepository {
    void saveExitInfo(Long userId, String exitReason);
}