package com.swyp3.babpool.domain.profile.application.response;

import com.swyp3.babpool.domain.profile.dao.ProfileDetailDaoDto;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class ProfileDetailResponse {
    private Long profileId;
    private Long userId;
    private String name;
    private String profileImg;
    private String grade;
    private String intro;
    private String contents;
    private String keywords;
    private Map<String,Integer> reviewCount;
    private List<String> reviews;

    public ProfileDetailResponse(ProfileDetailDaoDto profileDetailDaoDto, Map<String,Integer> reviewCount, List<String> reviews) {
        this.profileId=profileDetailDaoDto.getProfileId();
        this.userId=profileDetailDaoDto.getUserId();
        this.name=profileDetailDaoDto.getName();
        this.profileImg=profileDetailDaoDto.getProfileImg();
        this.grade=profileDetailDaoDto.getGrade();
        this.intro=profileDetailDaoDto.getIntro();
        this.contents=profileDetailDaoDto.getContents();
        this.keywords=profileDetailDaoDto.getKeywords();
        this.reviewCount=reviewCount;
        this.reviews=reviews;
    }
}