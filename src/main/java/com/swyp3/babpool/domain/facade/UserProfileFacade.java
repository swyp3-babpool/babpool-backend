package com.swyp3.babpool.domain.facade;

import com.swyp3.babpool.domain.profile.api.request.ProfileUpdateRequest;
import com.swyp3.babpool.domain.profile.application.ProfileService;
import com.swyp3.babpool.domain.profile.application.response.ProfileUpdateResponse;
import com.swyp3.babpool.domain.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserProfileFacade {

    private final ProfileService profileService;
    private final UserService userService;


    @Transactional
    public ProfileUpdateResponse updateProfileInfo(Long userId, ProfileUpdateRequest profileUpdateRequest) {
        ProfileUpdateResponse profileUpdateResponse = profileService.updateProfileInfo(userId, profileUpdateRequest);
        // 사용자 닉네임, 등급 수정
        userService.updateUserNickNameAndGrade(userId, profileUpdateRequest.getUserNickName(), profileUpdateRequest.getUserGrade());
        return profileUpdateResponse;
    }
}
