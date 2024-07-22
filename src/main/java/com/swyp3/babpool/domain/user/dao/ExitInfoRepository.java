package com.swyp3.babpool.domain.user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface ExitInfoRepository {

    // 테스트 코드 작성 완료
    void saveExitInfo(@Param("exitId") Long exitId, @Param("userId") Long userId, @Param("exitReason") String exitReason);

    // 테스트 코드 작성 완료
    Map<String, Object> findById(Long exitId);
}
