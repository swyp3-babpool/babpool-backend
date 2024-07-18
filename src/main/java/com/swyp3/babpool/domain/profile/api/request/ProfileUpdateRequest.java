package com.swyp3.babpool.domain.profile.api.request;

import com.swyp3.babpool.domain.profile.domain.Profile;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@ToString
public class ProfileUpdateRequest {

    private String userNickName;
    private String userGrade;
    private String profileIntro;
    private String profileContents;
    private String profileContactPhone;
    private String profileContactChat;
    private List<Long> keywords;

    @Builder
    public ProfileUpdateRequest(String userNickName, String userGrade, String profileIntro, String profileContents, String profileContactPhone, String profileContactChat, List<Long> keywords) {
        this.userNickName = userNickName;
        this.userGrade = userGrade;
        this.profileIntro = profileIntro;
        this.profileContents = profileContents;
        this.profileContactPhone = profileContactPhone;
        this.profileContactChat = profileContactChat;
        this.keywords = keywords;
    }

}
