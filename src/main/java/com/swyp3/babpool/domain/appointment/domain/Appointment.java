package com.swyp3.babpool.domain.appointment.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class Appointment {

    private Long appointmentId;
    private Long appointmentRequesterUserId;
    private Long appointmentReceiverUserId;
    private Long appointmentFixTimeId;
    private String appointmentStatus;
    private LocalDateTime appointmentCreateDate;
    private LocalDateTime appointmentModifyDate;

    @Builder
    public Appointment(Long appointmentId, Long appointmentRequesterUserId, Long appointmentReceiverUserId, Long appointmentFixTimeId, String appointmentStatus, LocalDateTime appointmentCreateDate, LocalDateTime appointmentModifyDate) {
        this.appointmentId = appointmentId;
        this.appointmentRequesterUserId = appointmentRequesterUserId;
        this.appointmentReceiverUserId = appointmentReceiverUserId;
        this.appointmentFixTimeId = appointmentFixTimeId;
        this.appointmentStatus = appointmentStatus;
        this.appointmentCreateDate = appointmentCreateDate;
        this.appointmentModifyDate = appointmentModifyDate;
    }
}
