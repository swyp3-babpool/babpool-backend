package com.swyp3.babpool.domain.appointment.application.response.appointmentdetail;

import com.swyp3.babpool.domain.user.application.response.MyPageUserDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class AppointmentDetailResponse {
    private String userNickName;
    private String userGrade;
    private String profileIntro;
    private String profileImageUrl;
    private String[] keywords;
    private LocalDateTime possibleDateTime;
    private String appointmentContent;

    public AppointmentDetailResponse(MyPageUserDto userData, LocalDateTime possibleDateTime, String appointmentContent) {
        this.userNickName = userData.getName();
        this.userGrade = userData.getGrade();
        this.profileIntro = userData.getIntro();
        this.profileImageUrl = userData.getProfileImg();
        this.keywords = userData.getKeywords();
        this.possibleDateTime = possibleDateTime;
        this.appointmentContent = appointmentContent;
    }
}
