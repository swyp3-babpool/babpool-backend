package com.swyp3.babpool.domain.user.dao;

import com.swyp3.babpool.domain.user.application.response.MyPageUserDto;
import com.swyp3.babpool.domain.user.domain.User;
import com.swyp3.babpool.domain.user.domain.UserStatus;
import com.swyp3.babpool.infra.auth.AuthPlatform;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRepository {

    // 테스트 코드 작성 완료
    Integer save(User user);

    // 테스트 코드 작성 완료
    Long findUserIdByPlatformAndPlatformId(@Param("platformName") AuthPlatform authPlatform,@Param("platformId") String platformId);

    // 테스트 코드 작성 완료
    User findById(Long userId);

    // 테스트 코드 작성 완료
    void updateSignUpInfo(@Param("userId") Long userId, @Param("userGrade") String userGrade);


    MyPageUserDto findMyProfile(Long userId);

    // 테스트 코드 작성 완료
    String findUserGradeById(Long userId);

    // 테스트 코드 작성 완료
    int updateUserStateByUserId(@Param("userId") Long userId, @Param("userStatus") UserStatus userStatus);

    // 테스트 코드 작성 완료
    User findActiveUserByUserEmail(String userEmail);

    // 테스트 코드 작성 완료
    void updateUserNickNameAndGrade(Long userId, String userNickName, String userGrade);
}
