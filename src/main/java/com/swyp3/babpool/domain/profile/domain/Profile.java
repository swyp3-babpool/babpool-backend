package com.swyp3.babpool.domain.profile.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Profile {

    private Long profileId;
    private Long userId;
    private String profileImageUrl;
    private String profileIntro;
    private String profileContents;
    private String profileContactPhone;
    private String profileContactChat;
    private Boolean profileActiveFlag;

    @Builder
    public Profile(Long profileId, Long userId, String profileImageUrl, String profileIntro, String profileContents, String profileContactPhone, String profileContactChat, Boolean profileActiveFlag) {
        this.profileId = profileId;
        this.userId = userId;
        this.profileImageUrl = profileImageUrl;
        this.profileIntro = profileIntro;
        this.profileContents = profileContents;
        this.profileContactPhone = profileContactPhone;
        this.profileContactChat = profileContactChat;
        this.profileActiveFlag = profileActiveFlag;
    }
}
