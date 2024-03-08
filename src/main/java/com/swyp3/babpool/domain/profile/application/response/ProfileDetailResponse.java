package com.swyp3.babpool.domain.profile.application.response;

import com.swyp3.babpool.domain.review.application.response.ReviewCountByTypeResponse;
import lombok.Getter;

import java.util.HashMap;
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
    private Map<String,Long> reviewCount;

    public ProfileDetailResponse(ProfileDetailDaoDto profileDetailDaoDto, ReviewCountByTypeResponse reviewCount) {
        this.profileId=profileDetailDaoDto.getProfileId();
        this.name=profileDetailDaoDto.getName();
        this.profileImg=profileDetailDaoDto.getProfileImg();
        this.grade=profileDetailDaoDto.getGrade();
        this.intro=profileDetailDaoDto.getIntro();
        this.contents=profileDetailDaoDto.getContents();
        this.keywords = profileDetailDaoDto.getKeywords().split(",");

        Map<String, Long> reviewCountMap = new HashMap<>();
        reviewCountMap.put("best",reviewCount.getBestCount());
        reviewCountMap.put("good",reviewCount.getGreatCount());
        reviewCountMap.put("bad",reviewCount.getBadCount());
        this.reviewCount=reviewCountMap;
    }
}