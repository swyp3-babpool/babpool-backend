package com.swyp3.babpool.domain.appointment.application.response.appointmentdetail;

import com.swyp3.babpool.domain.appointment.application.response.AppointmentRequesterPossibleDateTimeResponse;
import com.swyp3.babpool.domain.user.application.response.MyPageUserDto;
import lombok.Getter;

import java.util.List;

@Getter
public abstract class AppointmentDetailResponse {
    private String userNickName;
    private String userGrade;
    private String profileIntro;
    private String profileImgUrl;
    private String[] keywords;
    private List<AppointmentRequesterPossibleDateTimeResponse> possibleDateTimes;
    private String question;

    public AppointmentDetailResponse(MyPageUserDto userData, List<AppointmentRequesterPossibleDateTimeResponse> possibleDateTimes, String question) {
        this.userNickName = userData.getName();
        this.userGrade = userData.getGrade();
        this.profileIntro = userData.getIntro();
        this.profileImgUrl = userData.getProfileImg();
        this.keywords = userData.getKeywords();
        this.possibleDateTimes = possibleDateTimes;
        this.question = question;
    }
}
