package com.swyp3.babpool.domain.appointment.application.response.appointmentdetail;

import com.swyp3.babpool.domain.appointment.application.response.AppointmentRequesterPossibleDateTimeResponse;
import com.swyp3.babpool.domain.user.application.response.MyPageUserDto;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class AppointmentSendWaitingDetailResponse extends AppointmentDetailResponse {
    private Map<String,Long> lastingTime;
    private String contactChat;
    private String contactPhone;

    public AppointmentSendWaitingDetailResponse(MyPageUserDto userData, Map<String, Long> lastingTime, List<AppointmentRequesterPossibleDateTimeResponse> requesterPossibleTime, String question) {
        super(userData,requesterPossibleTime,question);
        this.lastingTime=lastingTime;
        this.contactChat=userData.getContactChat();
        this.contactPhone=userData.getContactPhone();
    }
}
