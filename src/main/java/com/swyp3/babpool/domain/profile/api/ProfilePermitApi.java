package com.swyp3.babpool.domain.profile.api;

import com.swyp3.babpool.domain.profile.api.request.ProfileListRequest;
import com.swyp3.babpool.domain.profile.application.ProfileService;
import com.swyp3.babpool.domain.profile.application.response.ProfileResponse;
import com.swyp3.babpool.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfilePermitApi {

    private final ProfileService profileService;

    @GetMapping("/api/profile/list")
    public ApiResponse<List<ProfileResponse>> getProfileList(@RequestParam String searchTerm,
                                                             @RequestParam List<String> keywords) {
        return ApiResponse.ok(profileService.getProfileListInConditionsOf(ProfileListRequest.builder()
                .searchTerm(searchTerm)
                .keywords(keywords)
                .build()));
    }
}
