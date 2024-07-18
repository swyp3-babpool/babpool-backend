package com.swyp3.babpool.domain.profile.dao;

import com.swyp3.babpool.domain.profile.api.request.ProfileUpdateRequest;
import com.swyp3.babpool.domain.profile.domain.Profile;
import com.swyp3.babpool.domain.user.dao.UserRepository;
import com.swyp3.babpool.domain.user.domain.User;
import com.swyp3.babpool.domain.user.domain.UserRole;
import com.swyp3.babpool.domain.user.domain.UserStatus;
import com.swyp3.babpool.global.tsid.TsidKeyGenerator;
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
    @Autowired
    private UserRepository userRepository;
    private TsidKeyGenerator tsidKeyGenerator = new TsidKeyGenerator();

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

    @DisplayName("updateProfileByProfileIdAndUpdateRequestDto 매퍼는 프로필 식별값과 업데이트 요청 DTO로 프로필을 업데이트한다.")
    @Test
    void updateProfileByProfileIdAndUpdateRequestDto() {
        // given
        Long profileId = 200000000000000001L;
        ProfileUpdateRequest profileUpdateRequest = ProfileUpdateRequest.builder()
                .profileIntro("abc")
                .profileContents("efg")
                .profileContactPhone("01012345678")
                .profileContactChat("test@chat.com")
                .build();

        // when
        profileRepository.updateProfileByProfileIdAndUpdateRequestDto(profileId, profileUpdateRequest);

        // then
        Profile profile = profileRepository.findById(profileId);
        assertThat(profile.getProfileIntro()).isEqualTo("abc");
        assertThat(profile.getProfileContactPhone()).isEqualTo("01012345678");
    }

    @DisplayName("updateProfileImageUrl 매퍼는 프로필 이미지 URL을 수정한다.")
    @Test
    void updateProfileImageUrl() {
        // given
        Long userId = 100000000000000001L;
        String targetProfileImageUrl = "https://updatedurl.com";

        // when
        profileRepository.updateProfileImageUrl(userId, targetProfileImageUrl);

        // then
        Profile profile = profileRepository.findByUserId(userId);
        assertThat(profile.getProfileImageUrl()).isEqualTo("https://updatedurl.com");
    }

    @DisplayName("saveProfile 매퍼는 프로필을 저장한다.")
    @Test
    void saveProfile() {
        // given
        Long targetUserId = tsidKeyGenerator.generateTsid();
        userRepository.save(User.allArgsBuilder()
                .userId(targetUserId)
                .userEmail("temp@gmail.com")
                .userStatus(UserStatus.PREACTIVE)
                .userRole(UserRole.USER)
                .userGrade("FIRST_GRADE")
                .allArgsBuild());

        Long targetProfileId = tsidKeyGenerator.generateTsid();
        Profile targetProfile = Profile.builder()
                .profileId(targetProfileId)
                .userId(targetUserId)
                .profileImageUrl("https://profileimage.com")
                .profileActiveFlag(false)
                .build();

        // when
        profileRepository.saveProfile(targetProfile);

        // then
        Profile savedProfile = profileRepository.findById(targetProfileId);
        assertThat(savedProfile.getUserId()).isEqualTo(targetUserId);
    }


}