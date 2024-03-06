package com.swyp3.babpool.domain.user.application.response;

import com.swyp3.babpool.domain.profile.application.response.ProfileDetailDaoDto;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class MyPageResponse {
    //마이페이지 조회 최종 응답 DTO
    private Long profileId;
    private String name;
    private String profileImg;
    private String grade;
    private String intro;
    private String keywords;
    private Map<String,Integer> reviewCount;

    //TODO: 밥약 히스토리 DTO 리스트로 변경 필요
    private List<String> histories;

    public MyPageResponse(MyPageUserDaoDto myPageUserDaoDto, Map<String,Integer> reviewCount, List<String> histories) {
        this.profileId = myPageUserDaoDto.getProfileId();
        this.name=myPageUserDaoDto.getName();
        this.profileImg= myPageUserDaoDto.getProfileImg();
        this.grade= myPageUserDaoDto.getGrade();
        this.intro= myPageUserDaoDto.getIntro();
        this.keywords= myPageUserDaoDto.getKeywords();
        this.reviewCount=reviewCount;
        this.histories=histories;
    }
}
