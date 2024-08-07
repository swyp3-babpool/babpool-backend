package com.swyp3.babpool.domain.profile.application.response;

import com.swyp3.babpool.domain.profile.domain.ProfileDetail;
import com.swyp3.babpool.domain.review.application.response.ReviewCountByTypeResponse;
import com.swyp3.babpool.domain.review.application.response.ReviewPagingResponse;
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
    private Map<String, Integer> reviewCount;
    private List<ReviewPagingResponse> reviews;
    private Boolean isApiRequesterSameAsProfileOwner = false;

    public ProfileDetailResponse(ProfileDetail profileDetail, ReviewCountByTypeResponse reviewCount, List<ReviewPagingResponse> reviews) {
        this.profileId= profileDetail.getProfileId();
        this.name= profileDetail.getName();
        this.profileImg= profileDetail.getProfileImg();
        this.grade= profileDetail.getGrade();
        this.intro= profileDetail.getIntro();
        this.contents= profileDetail.getContents();
        this.keywords = profileDetail.getKeywords().split(",");

        Map<String, Integer> reviewCountMap = new HashMap<>();
        reviewCountMap.put("best", reviewCount.getBestCount());
        reviewCountMap.put("good", reviewCount.getGreatCount());
        reviewCountMap.put("bad", reviewCount.getBadCount());
        this.reviewCount=reviewCountMap;
        this.reviews=reviews;
    }

    public void setApiRequesterSameAsProfileOwner(Boolean apiRequesterSameAsProfileOwner) {
        this.isApiRequesterSameAsProfileOwner = apiRequesterSameAsProfileOwner;
    }
}