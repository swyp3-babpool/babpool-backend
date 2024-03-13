package com.swyp3.babpool.domain.appointment.application.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AppointmentAcceptResponse {
    private String requesterNickName;
    private String requesterProfileImageUrl;
    private String requesterGrade;
    private String requesterIntro;
    private LocalDateTime fixDateTime;
    private String requesterContactPhone;
    private String requesterContactChat;
    private String question;
}
