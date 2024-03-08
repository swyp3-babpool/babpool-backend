package com.swyp3.babpool.domain.profile.application.response;

import com.swyp3.babpool.domain.profile.domain.ProfileDefault;
import lombok.Getter;

import java.util.Map;

@Getter
public class ProfileDefaultResponse {
    private String userNickName;
    private String userGrade;
    private String imgUrl;
    private String profileIntro;
    private String profileContents;
    private String profileContactPhone;
    private String profileContactChat;
    private Map<String,String[]> keywords;


    public ProfileDefaultResponse(ProfileDefault daoResponse, ProfileKeywordsResponse keywords) {
        this.userNickName = daoResponse.getUserNickName();
        this.userGrade = daoResponse.getUserGrade();
        this.imgUrl = daoResponse.getImgUrl();
        this.profileIntro = daoResponse.getProfileIntro();
        this.profileContents = daoResponse.getProfileContents();
        this.profileContactPhone = daoResponse.getProfileContactPhone();
        this.profileContactChat = daoResponse.getProfileContactChat();
        this.keywords = keywords.getKeywords();
    }
}
