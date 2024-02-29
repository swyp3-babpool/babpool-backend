package com.swyp3.babpool.domain.profile.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ProfileUpdateRequest {

    private String profileIntro;
    private String profileContents;
    private String profileContactPhone;
    private String profileContactChat;

    @Builder
    public ProfileUpdateRequest(String profileIntro, String profileContents, String profileContactPhone, String profileContactChat) {
        this.profileIntro = profileIntro;
        this.profileContents = profileContents;
        this.profileContactPhone = profileContactPhone;
        this.profileContactChat = profileContactChat;
    }
}
