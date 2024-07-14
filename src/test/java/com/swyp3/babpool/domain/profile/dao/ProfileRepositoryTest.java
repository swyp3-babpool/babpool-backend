package com.swyp3.babpool.domain.profile.dao;

import com.swyp3.babpool.domain.profile.domain.Profile;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@MybatisTest
class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;

    @DisplayName("findUserIdByProfileId 매퍼는 프로필 식별 값으로 사용자 식별 값을 조회한다.")
    @Test
    void findUserIdByProfileId() {
        // given
        Long targetProfileId = 200000000000000001L;

        // when
        Long userId = profileRepository.findUserIdByProfileId(targetProfileId);

        // then
        assertThat(userId).isEqualTo(100000000000000001L);
    }

    @DisplayName("findById 매퍼는 프로필 식별 값으로 프로필을 조회한다.")
    @Test
    void findById() {
        // given
        Long profileId = 200000000000000001L;

        // when
        Profile profile = profileRepository.findById(profileId);

        // then
        assertThat(profile.getUserId()).isEqualTo(100000000000000001L);
    }

    @DisplayName("findByUserId 매퍼는 사용자 식별 값으로 프로필을 조회한다.")
    @Test
    void findByUserId() {
        // given
        Long userId = 100000000000000001L;

        // when
        Profile profile = profileRepository.findByUserId(userId);

        // then
        assertThat(profile.getProfileId()).isEqualTo(200000000000000001L);
    }

    @DisplayName("updateProfileActiveFlag 매퍼는 프로필 활성화 상태를 변경한다.")
    @Test
    void updateProfileActiveFlag() {
        // given
        Long userId = 100000000000000001L;
        Boolean targetActiveFlag = false;

        // when
        profileRepository.updateProfileActiveFlag(userId, targetActiveFlag);

        // then
        Profile profile = profileRepository.findByUserId(userId);
        assertThat(profile.getProfileActiveFlag()).isFalse();
    }


}