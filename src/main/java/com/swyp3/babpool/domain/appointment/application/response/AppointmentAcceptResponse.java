package com.swyp3.babpool.domain.appointment.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.sql.Date;
import java.time.LocalDateTime;

@ToString
@Getter
public class AppointmentAcceptResponse {

    private Long appointmentId;
    private String requesterNickName;
    private String requesterProfileImageUrl;
    private String requesterGrade;
    private String requesterIntro;
    private LocalDateTime possibleDateTime;
    private String requesterContactPhone;
    private String requesterContactChat;
    private String appointmentContent;

    @Builder
    public AppointmentAcceptResponse(Long appointmentId, String requesterNickName, String requesterProfileImageUrl, String requesterGrade, String requesterIntro, LocalDateTime possibleDateTime, String requesterContactPhone, String requesterContactChat, String appointmentContent) {
        this.appointmentId = appointmentId;
        this.requesterNickName = requesterNickName;
        this.requesterProfileImageUrl = requesterProfileImageUrl;
        this.requesterGrade = requesterGrade;
        this.requesterIntro = requesterIntro;
        this.possibleDateTime = possibleDateTime;
        this.requesterContactPhone = requesterContactPhone;
        this.requesterContactChat = requesterContactChat;
        this.appointmentContent = appointmentContent;
    }
}
