package com.swyp3.babpool.domain.profile.application;

import com.swyp3.babpool.domain.profile.api.request.ProfilePagingConditions;
import com.swyp3.babpool.domain.profile.api.request.ProfileUpdateRequest;
import com.swyp3.babpool.domain.profile.application.response.ProfilePagingResponse;
import com.swyp3.babpool.domain.profile.domain.Profile;
import com.swyp3.babpool.domain.profile.application.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;



public interface ProfileService {

    void createInitProfile(Profile profile);

    Profile getByUserId(Long userId);

    String updateProfileImage(Long userId, MultipartFile multipartFile);

    Long updateProfileInfo(Long userId, ProfileUpdateRequest profileUpdateRequest);

    Page<ProfilePagingResponse> getProfileListWithPageable(ProfilePagingConditions profilePagingConditions, Pageable pageable);

    ProfileDetailResponse getProfileDetail(Long userId, Long targetProfileId);

    ProfileDefaultResponse getProfileDefault(Long userId);

    ProfileRegistrationResponse getProfileisRegistered(Long userId);

    void updateProfileActiveFlag(Long userId, Boolean activeFlag);

    Profile getProfileByProfileId(Long profileId);

    void updateProfileImageFromSocialProfileImage(Long userId, String profileImage);
}
