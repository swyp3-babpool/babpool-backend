package com.swyp3.babpool.domain.appointment.application.response;

import com.swyp3.babpool.domain.user.application.response.MyPageUserDaoDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
public class AppointmentDetailResponse {
    private String userNickName;
    private String userGrade;
    private String profileIntro;
    private String profileImgUrl;
    private String[] keywords;
    private Map<String,Long> lastingTime;
    private List<AppointmentRequesterPossibleDateTimeResponse> possibleDateTimes;
    private String question;

    public AppointmentDetailResponse(MyPageUserDaoDto requesterData, Map<String, Long> lastingTime, List<AppointmentRequesterPossibleDateTimeResponse> requesterPossibleTime, String question) {
       this.userNickName=requesterData.getName();
        this.userGrade=requesterData.getGrade();
        this.profileIntro=requesterData.getIntro();
        this.profileImgUrl=requesterData.getProfileImg();
        this.keywords=requesterData.getKeywords();
        this.lastingTime=lastingTime;
        this.possibleDateTimes=requesterPossibleTime;
        this.question=question;
    }
}
