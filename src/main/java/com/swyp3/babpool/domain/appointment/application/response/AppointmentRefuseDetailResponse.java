package com.swyp3.babpool.domain.appointment.application.response;

import com.swyp3.babpool.domain.user.application.response.MyPageUserDto;
import lombok.Getter;

import java.util.List;

@Getter
public class AppointmentRefuseDetailResponse {
    private String receiverNickName;
    private String receiverProfileImage;
    private String receiverGrade;
    private String receiverProfileIntro;
    private String[] keywords;
    private String message;

    public AppointmentRefuseDetailResponse(String receiverNickName, String receiverProfileImage, String receiverGrade,
                                           String receiverProfileIntro, String keywords, String message) {
        this.receiverNickName = receiverNickName;
        this.receiverProfileImage = receiverProfileImage;
        this.receiverGrade = receiverGrade;
        this.receiverProfileIntro = receiverProfileIntro;
        this.message = message;
        this.keywords = keywords.split(",");
    }
}
