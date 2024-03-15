package com.swyp3.babpool.domain.appointment.application.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;

@Getter
public class AppointmentAcceptResponse {
    private Long appointmentId;
    private String requesterNickName;
    private String requesterProfileImageUrl;
    private String requesterGrade;
    private String requesterIntro;
    private Date date;
    private Long time;
    private String requesterContactPhone;
    private String requesterContactChat;
    private String question;

}
