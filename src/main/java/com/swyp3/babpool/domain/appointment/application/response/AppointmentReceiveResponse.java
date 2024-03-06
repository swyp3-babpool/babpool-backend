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
    private String appointmentFixDateTime;
    private String appointmentStatus;
    private LocalDateTime appointmentCreateDate;

    @Builder
    public AppointmentReceiveResponse(Long appointmentId, Long appointmentSenderProfileId, String appointmentSenderUserNickname, String appointmentSenderProfileImageUrl, String appointmentFixDateTime, String appointmentStatus, LocalDateTime appointmentCreateDate) {
        this.appointmentId = appointmentId;
        this.appointmentSenderProfileId = appointmentSenderProfileId;
        this.appointmentSenderUserNickname = appointmentSenderUserNickname;
        this.appointmentSenderProfileImageUrl = appointmentSenderProfileImageUrl;
        this.appointmentFixDateTime = appointmentFixDateTime;
        this.appointmentStatus = appointmentStatus;
        this.appointmentCreateDate = appointmentCreateDate;
    }
}
