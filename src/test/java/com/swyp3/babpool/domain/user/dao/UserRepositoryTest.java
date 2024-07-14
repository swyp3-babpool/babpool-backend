package com.swyp3.babpool.domain.user.dao;

import com.swyp3.babpool.domain.user.domain.User;
import com.swyp3.babpool.domain.user.domain.UserRole;
import com.swyp3.babpool.domain.user.domain.UserStatus;
import com.swyp3.babpool.global.tsid.TsidKeyGenerator;
import com.swyp3.babpool.infra.auth.AuthPlatform;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@MybatisTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private TsidKeyGenerator tsidKeyGenerator;

    @BeforeEach
    void setUp() {
        tsidKeyGenerator = new TsidKeyGenerator();
    }


    @DisplayName("save 매퍼는 사용자 정보를 저장한다.")
    @Test
    void save(){
        // given
        User createUser = User.allArgsBuilder()
                .userId(tsidKeyGenerator.generateTsid())
                .userEmail("newuser@test.com")
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.USER)
                .userGrade("FIRST")
                .userNickName("newuser")
                .userCreateDate(LocalDateTime.now())
                .userModifyDate(LocalDateTime.now())
                .allArgsBuild();

        log.info("createUser: {}", createUser);
        // when
        userRepository.save(createUser);

        // then
        User savedUser = userRepository.findActiveUserByUserEmail("newuser@test.com");
        assertNotNull(savedUser);
        assertEquals("newuser@test.com", savedUser.getUserEmail());
        assertEquals(UserStatus.ACTIVE, savedUser.getUserStatus());
        assertEquals(UserRole.USER, savedUser.getUserRole());
        assertEquals("FIRST", savedUser.getUserGrade());
        assertEquals("newuser", savedUser.getUserNickName());
    }

    @DisplayName("findUserIdByPlatformAndPlatformId 매퍼는 t_oauth테이블에서 platform과 platformId 으로 사용자 userId를 조회한다.")
    @Test
    void findUserIdByPlatformAndPlatformId(){
        // given
        AuthPlatform authPlatform = AuthPlatform.KAKAO;
        String platformId = "1000000001";

        // when
        Long userId = userRepository.findUserIdByPlatformAndPlatformId(authPlatform, platformId);

        // then
        Assertions.assertThat(userId).isEqualTo(100000000000000001L);
    }

    @DisplayName("findById 매퍼는 t_user 테이블에서 userId로 사용자 정보를 조회한다.")
    @Test
    void findById(){
        // given
        Long userId = 100000000000000001L;

        // when
        User user = userRepository.findById(userId);

        // then
        Assertions.assertThat(user.getUserId()).isEqualTo(100000000000000001L);
        Assertions.assertThat(user.getUserGrade()).isEqualTo("FIRST");
    }

    @DisplayName("updateSignUpInfo 매퍼는 특정 사용자의 학력을 수정한다.")
    @Test
    void updateSignUpInfo(){
        // given
        Long userId = 100000000000000001L;
        String userGrade = "SECOND";

        // when
        userRepository.updateSignUpInfo(userId, userGrade);

        // then
        String updatedUserGrade = userRepository.findUserGradeById(userId);
        Assertions.assertThat(updatedUserGrade).isEqualTo("SECOND");
    }

    @DisplayName("findUserGradeById 매퍼는 특정 사용자의 학력을 조회한다.")
    @Test
    void findUserGradeById(){
        // given
        Long userId = 100000000000000001L;

        // when
        String userGrade = userRepository.findUserGradeById(userId);

        // then
        Assertions.assertThat(userGrade).isEqualTo("FIRST");
    }

    @DisplayName("updateUserStateByUserId 매퍼는 특정 사용자의 상태를 수정한다.")
    @Test
    void updateUserStateByUserId(){
        // given
        Long userId = 100000000000000001L;
        UserStatus userStatus = UserStatus.EXIT;

        // when
        userRepository.updateUserStateByUserId(userId, userStatus);

        // then
        User user = userRepository.findById(userId);
        Assertions.assertThat(user.getUserStatus()).isEqualTo(UserStatus.EXIT);
    }

    @DisplayName("findActiveUserByUserEmail 매퍼는 특정 사용자의 이메일로 ACTIVE 상태의 사용자를 조회한다.")
    @Test
    void findActiveUserByUserEmail() {
        // given
        String targetUserEmail = "test1@test.com";

        // when
        User user = userRepository.findActiveUserByUserEmail(targetUserEmail);

        // then
        Assertions.assertThat(user.getUserStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @DisplayName("findActiveUserByUserEmail 매퍼는 ACTIVE 상태가 아닌 사용자는 조회하지 않는다.")
    @Test
    void findActiveUserByUserEmail_notActive() {
        // given
        User targetUser = User.allArgsBuilder()
                .userId(tsidKeyGenerator.generateTsid())
                .userEmail("temp@test.com")
                .userStatus(UserStatus.PREACTIVE)
                .userRole(UserRole.USER)
                .userGrade("FIRST")
                .userNickName("temp")
                .allArgsBuild();
        userRepository.save(targetUser);

        // when
        User user = userRepository.findActiveUserByUserEmail(targetUser.getUserEmail());

        // then
        assertNull(user);
    }

    @DisplayName("updateUserNickNameAndGrade 매퍼는 특정 사용자의 닉네임과 학력을 수정한다.")
    @Test
    void updateUserNickNameAndGrade(){
        // given
        Long userId = 100000000000000001L;
        String userNickName = "newNickName";
        String userGrade = "SECOND";

        // when
        userRepository.updateUserNickNameAndGrade(userId, userNickName, userGrade);

        // then
        User user = userRepository.findById(userId);
        Assertions.assertThat(user.getUserNickName()).isEqualTo("newNickName");
        Assertions.assertThat(user.getUserGrade()).isEqualTo("SECOND");
    }

}