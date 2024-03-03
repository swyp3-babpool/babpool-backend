package com.swyp3.babpool.domain.profile.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
@Getter
public class ProfilePagingResponse {

    private Long profileId;
    private Long userId;
    private String profileImageUrl;
    private String profileIntro;
    private String profileContents;
    private String profileContactPhone;
    private String profileContactChat;
    private Boolean profileActiveFlag;
    private String keywordIdWithNameString;
    private String userGrade;

    @Builder
    public ProfilePagingResponse(Long profileId, Long userId, String profileImageUrl, String profileIntro, String profileContents, String profileContactPhone, String profileContactChat, Boolean profileActiveFlag, String keywordIdWithNameString, String userGrade) {
        this.profileId = profileId;
        this.userId = userId;
        this.profileImageUrl = profileImageUrl;
        this.profileIntro = profileIntro;
        this.profileContents = profileContents;
        this.profileContactPhone = profileContactPhone;
        this.profileContactChat = profileContactChat;
        this.profileActiveFlag = profileActiveFlag;
        this.keywordIdWithNameString = keywordIdWithNameString;
        this.userGrade = userGrade;
    }


}

