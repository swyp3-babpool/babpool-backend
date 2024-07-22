package com.swyp3.babpool.domain.profile.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ProfileUpdateResponse {
    private Long profileId;
    private String profileImageUrl;

    @Builder
    public ProfileUpdateResponse(Long profileId, String profileImageUrl) {
        this.profileId = profileId;
        this.profileImageUrl = profileImageUrl;
    }
}
