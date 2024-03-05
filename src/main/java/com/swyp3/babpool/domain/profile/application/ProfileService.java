package com.swyp3.babpool.domain.profile.application;

import com.swyp3.babpool.domain.profile.api.request.ProfilePagingConditions;
import com.swyp3.babpool.domain.profile.api.request.ProfileUpdateRequest;
import com.swyp3.babpool.domain.profile.application.response.ProfilePagingDto;
import com.swyp3.babpool.domain.profile.application.response.ProfilePagingResponse;
import com.swyp3.babpool.domain.profile.application.response.ProfileUpdateResponse;
import com.swyp3.babpool.domain.profile.domain.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;



public interface ProfileService {
    //회원가입 시 프로필 생성
    void saveProfile(Profile profile);
    //사용자 지정 이미지로 수정
    String uploadProfileImage(Long userId, MultipartFile multipartFile);
    //사용자가 프로필 카드 업데이트
    ProfileUpdateResponse saveProfileInfo(Long userId, ProfileUpdateRequest profileUpdateRequest);

    Page<ProfilePagingResponse> getProfileListWithPageable(ProfilePagingConditions profilePagingConditions, Pageable pageable);

}
