package com.swyp3.babpool.domain.profile.application.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ProfileDetailDaoDto {
    // 후기 제외한 프로필 상세 조회 데이터
    private Long profileId;
    private String name;
    private String profileImg;
    private String grade;
    private String intro;
    private String contents;

    private String keywords;
}