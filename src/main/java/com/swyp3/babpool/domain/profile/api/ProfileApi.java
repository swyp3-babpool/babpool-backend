package com.swyp3.babpool.domain.profile.api;

import com.swyp3.babpool.domain.profile.api.request.ProfileUpdateRequest;
import com.swyp3.babpool.domain.profile.application.ProfileService;
import com.swyp3.babpool.domain.profile.application.response.ProfileDefaultResponse;
import com.swyp3.babpool.domain.profile.application.response.ProfileDetailResponse;
import com.swyp3.babpool.domain.profile.application.response.ProfileRegistrationResponse;
import com.swyp3.babpool.domain.profile.application.response.ProfileUpdateResponse;
import com.swyp3.babpool.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileApi {
    private final ProfileService profileService;

    @PostMapping("/update")
    public ApiResponse<ProfileUpdateResponse> updateProfileCard(@RequestAttribute(value = "userId") Long userId,
                                                                @RequestPart(value = "profileImageFile", required = false) MultipartFile multipartFile,
                                                                @RequestPart(value = "profileInfo") ProfileUpdateRequest profileUpdateRequest) {
        profileService.updateProfileImage(userId, multipartFile);
        ProfileUpdateResponse response =  profileService.updateProfileInfo(userId, profileUpdateRequest);
        return ApiResponse.ok(response);
    }

    @GetMapping("/default")
    public ApiResponse<ProfileDefaultResponse> getProfileDefault(@RequestAttribute(value = "userId") Long userId){
        ProfileDefaultResponse response = profileService.getProfileDefault(userId);
        return ApiResponse.ok(response);
    }

    @GetMapping("/detail/{targetProfileId}")
    public ApiResponse<ProfileDetailResponse> getProfileDetail(@RequestAttribute(value = "userId") Long userId, @PathVariable(name="targetProfileId") Long targetProfileId){
        ProfileDetailResponse profileDetailResponse = profileService.getProfileDetail(userId, targetProfileId);
        return ApiResponse.ok(profileDetailResponse);
    }

    @GetMapping("/registration/check")
    public ApiResponse<ProfileRegistrationResponse> getProfileisRegistered(@RequestAttribute(value = "userId") Long userId){
        ProfileRegistrationResponse profileRegistrationResponse = profileService.getProfileisRegistered(userId);
        return ApiResponse.ok(profileRegistrationResponse);
    }
}
