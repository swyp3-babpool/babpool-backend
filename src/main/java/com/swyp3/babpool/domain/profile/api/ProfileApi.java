package com.swyp3.babpool.domain.profile.api;

import com.swyp3.babpool.domain.profile.api.request.ProfileUpdateRequest;
import com.swyp3.babpool.domain.profile.application.ProfileService;
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

    @PostMapping("/card")
    public ApiResponse<ProfileUpdateResponse> updateProfileCard(@RequestAttribute(value = "userId") Long userId,
                                                                @RequestPart(value = "profileImageFile") MultipartFile multipartFile,
                                                                @RequestPart(value = "profileInfo") ProfileUpdateRequest profileUpdateRequest) {
        profileService.uploadProfileImage(userId, multipartFile);
        ProfileUpdateResponse profileResponse = profileService.saveProfileInfo(userId, profileUpdateRequest);
        return ApiResponse.ok(profileResponse);
    }

    @GetMapping("/detail/{userId}")
    public ApiResponse<ProfileDetailResponse> getProfileDetail(@RequestAttribute(value = "userId") Long userId){
        profileService.getProfileDetail()
    }
}
