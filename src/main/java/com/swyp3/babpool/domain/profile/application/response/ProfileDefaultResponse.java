package com.swyp3.babpool.domain.profile.application.response;

import lombok.Getter;

import java.util.List;
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


    public ProfileDefaultResponse(ProfileDefaultDaoDto daoResponse, KeywordsResponse keywords) {
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
