package com.swyp3.babpool.domain.profile.application.response;

import com.swyp3.babpool.domain.profile.domain.ProfileDetail;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class ProfileDetailResponse {
    private Long profileId;
    private String name;
    private String profileImg;
    private String grade;
    private String intro;
    private String contents;
    private String[] keywords;
    private Map<String,Integer> reviewCount;
    private List<String> reviews;

    public ProfileDetailResponse(ProfileDetail profileDetail, Map<String,Integer> reviewCount, List<String> reviews) {
        this.profileId= profileDetail.getProfileId();
        this.name= profileDetail.getName();
        this.profileImg= profileDetail.getProfileImg();
        this.grade= profileDetail.getGrade();
        this.intro= profileDetail.getIntro();
        this.contents= profileDetail.getContents();
        this.keywords = profileDetail.getKeywords().split(",");
        this.reviewCount=reviewCount;
        this.reviews=reviews;
    }
}