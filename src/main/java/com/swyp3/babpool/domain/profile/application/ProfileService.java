package com.swyp3.babpool.domain.profile.application;

import com.swyp3.babpool.domain.profile.api.request.ProfilePagingConditions;
import com.swyp3.babpool.domain.profile.api.request.ProfileUpdateRequest;
import com.swyp3.babpool.domain.profile.application.response.ProfilePagingResponse;
import com.swyp3.babpool.domain.profile.application.response.ProfileResponse;
import com.swyp3.babpool.domain.profile.application.response.ProfileUpdateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProfileService {

    String uploadProfileImage(Long userId, MultipartFile multipartFile);

    ProfileUpdateResponse saveProfileInfo(Long userId, ProfileUpdateRequest profileUpdateRequest);

    Page<ProfilePagingResponse> getProfileListWithPageable(ProfilePagingConditions profilePagingConditions, Pageable pageable);

}
