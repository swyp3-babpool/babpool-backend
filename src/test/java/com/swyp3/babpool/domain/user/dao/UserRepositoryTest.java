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
}