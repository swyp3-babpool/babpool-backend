package com.swyp3.babpool.domain.profile.application;

import com.swyp3.babpool.domain.profile.api.request.ProfilePagingConditions;
import com.swyp3.babpool.domain.profile.api.request.ProfileUpdateRequest;
import com.swyp3.babpool.domain.profile.application.response.ProfilePagingResponse;
import com.swyp3.babpool.domain.profile.domain.Profile;
import com.swyp3.babpool.domain.profile.application.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;



public interface ProfileService {
    //회원가입 시 프로필 생성
    void saveProfile(Profile profile);
    Profile getByUserId(Long userId);
    //사용자 지정 이미지로 수정
    String updateProfileImage(Long userId, MultipartFile multipartFile);
    //사용자가 프로필 카드 업데이트
    ProfileUpdateResponse updateProfileInfo(Long userId, ProfileUpdateRequest profileUpdateRequest);

    Page<ProfilePagingResponse> getProfileListWithPageable(ProfilePagingConditions profilePagingConditions, Pageable pageable);

    ProfileDetailResponse getProfileDetail(Long userId, Long targetProfileId);

    ProfileDefaultResponse getProfileDefault(Long userId);

    ProfileRegistrationResponse getProfileisRegistered(Long userId);

    void updateProfileActiveFlag(Long userId, Boolean activeFlag);

    void updatePossibleDateTime(Long userId, Long profileId, ProfileUpdateRequest profileUpdateRequest);
}
