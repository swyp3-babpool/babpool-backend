package com.swyp3.babpool.domain.appointment.application.response.appointmentdetail;

import com.swyp3.babpool.domain.user.application.response.MyPageUserDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AppointmentAcceptDetailResponse extends AppointmentDetailResponse{
    private String contactChat;
    private String contactPhone;
    private Long fixedDateTimeId;

    public AppointmentAcceptDetailResponse(MyPageUserDto userData, LocalDateTime possibleDateTime,
                                           String question,Long fixedDateTimeId) {
        super(userData,possibleDateTime,question);
        this.contactChat= userData.getContactChat();
        this.contactPhone=userData.getContactPhone();
        this.fixedDateTimeId=fixedDateTimeId;
    }
}
