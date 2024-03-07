package com.swyp3.babpool.domain.user.application.response;

import lombok.Getter;

@Getter
public class MyPageUserDaoDto {
    //마이페이지 조회 데이터 중 밥약 히스토리와 후기 개수 제외한 DTO
    private Long profileId;
    private String name;
    private String profileImg;
    private String grade;
    private String intro;
    private String[] keywords;

    public MyPageUserDaoDto(Long profileId, String name, String profileImg, String grade, String intro, String keywords) {
        this.profileId = profileId;
        this.name = name;
        this.profileImg = profileImg;
        this.grade = grade;
        this.intro = intro;
        this.keywords = keywords.split(",");
    }
}
