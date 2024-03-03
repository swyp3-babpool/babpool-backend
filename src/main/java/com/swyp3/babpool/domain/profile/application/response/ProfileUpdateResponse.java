package com.swyp3.babpool.domain.profile.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ProfileUpdateResponse {

    private String profileImageUrl;
    private String profileIntro;
    private String profileContents;
    private String profileContactPhone;
    private String profileContactChat;

    @Builder
    public ProfileUpdateResponse(String profileImageUrl, String profileIntro, String profileContents, String profileContactPhone, String profileContactChat) {
        this.profileImageUrl = profileImageUrl;
        this.profileIntro = profileIntro;
        this.profileContents = profileContents;
        this.profileContactPhone = profileContactPhone;
        this.profileContactChat = profileContactChat;
    }
}
