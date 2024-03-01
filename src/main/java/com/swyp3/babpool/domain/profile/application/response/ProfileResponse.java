package com.swyp3.babpool.domain.profile.application.response;

import com.swyp3.babpool.domain.profile.domain.Profile;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ProfileResponse {

    private Long profileId;
    private String profileImageUrl;
    private String profileIntro;
    private String profileContents;
    private String profileContactPhone;
    private String profileContactChat;

    @Builder
    public ProfileResponse(Long profileId, String profileImageUrl, String profileIntro, String profileContents, String profileContactPhone, String profileContactChat) {
        this.profileId = profileId;
        this.profileImageUrl = profileImageUrl;
        this.profileIntro = profileIntro;
        this.profileContents = profileContents;
        this.profileContactPhone = profileContactPhone;
        this.profileContactChat = profileContactChat;
    }

    public static ProfileResponse from(Profile profile) {
        return ProfileResponse.builder()
                .profileId(profile.getProfileId())
                .profileImageUrl(profile.getProfileImageUrl())
                .profileIntro(profile.getProfileIntro())
                .profileContents(profile.getProfileContents())
                .profileContactPhone(profile.getProfileContactPhone())
                .profileContactChat(profile.getProfileContactChat())
                .build();
    }
}
