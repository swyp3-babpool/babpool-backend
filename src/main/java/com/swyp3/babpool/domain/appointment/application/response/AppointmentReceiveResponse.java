package com.swyp3.babpool.domain.appointment.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class AppointmentReceiveResponse {

    private Long appointmentId;
    private Long appointmentSenderProfileId;
    private String appointmentSenderUserNickname;
    private String appointmentSenderProfileImageUrl;
    private String appointmentStatus;
    private LocalDateTime appointmentCreateDate;
    private String appointmentFixDateTime;

    @Builder
    public AppointmentReceiveResponse(Long appointmentId, Long appointmentSenderProfileId, String appointmentSenderUserNickname, String appointmentSenderProfileImageUrl, String appointmentStatus, LocalDateTime appointmentCreateDate, String appointmentFixDateTime) {
        this.appointmentId = appointmentId;
        this.appointmentSenderProfileId = appointmentSenderProfileId;
        this.appointmentSenderUserNickname = appointmentSenderUserNickname;
        this.appointmentSenderProfileImageUrl = appointmentSenderProfileImageUrl;
        this.appointmentStatus = appointmentStatus;
        this.appointmentCreateDate = appointmentCreateDate;
        this.appointmentFixDateTime = appointmentFixDateTime;
    }
}
