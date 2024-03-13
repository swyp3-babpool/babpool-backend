package com.swyp3.babpool.domain.appointment.application.response.appointmentdetail;

import com.swyp3.babpool.domain.appointment.application.response.AppointmentRequesterPossibleDateTimeResponse;
import com.swyp3.babpool.domain.user.application.response.MyPageUserDto;
import lombok.Getter;

import java.util.List;

@Getter
public class AppointmentAcceptDetailResponse extends AppointmentDetailResponse{
    private String contactChat;
    private String contactPhone;

    public AppointmentAcceptDetailResponse(MyPageUserDto userData, List<AppointmentRequesterPossibleDateTimeResponse> requesterPossibleTime, String question) {
        super(userData,requesterPossibleTime,question);
        this.contactChat= userData.getContactChat();
        this.contactPhone=userData.getContactPhone();
    }
}
