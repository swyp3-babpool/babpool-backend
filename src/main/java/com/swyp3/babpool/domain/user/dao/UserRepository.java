package com.swyp3.babpool.domain.user.dao;

import com.swyp3.babpool.domain.user.application.response.MyPageUserDaoDto;
import com.swyp3.babpool.domain.user.domain.User;
import com.swyp3.babpool.domain.user.domain.UserStatus;
import com.swyp3.babpool.infra.auth.AuthPlatform;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRepository {
    void save(User user);

    Long findUserIdByPlatformAndPlatformId(@Param("platformName") AuthPlatform authPlatform,@Param("platformId") String platformId);

    User findById(Long userId);

    void updateSignUpInfo(@Param("userId") Long userId, @Param("userGrade") String userGrade);

    void saveKeyword(@Param("userId") Long userId, @Param("keywordId") Long keywordId);

    MyPageUserDaoDto findMyProfile(Long userId);

    String findUserGradeById(Long userId);

    int updateUserStateByUserId(Long userId, UserStatus userStatus);
}
