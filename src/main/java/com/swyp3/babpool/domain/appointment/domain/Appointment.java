package com.swyp3.babpool.domain.appointment.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ToString
@Getter
public class Appointment {

    private Long appointmentId;
    private Long appointmentSenderId;
    private Long appointmentReceiverId;
    private Long possibleDateTimeId;  // FK
    private AppointmentStatus appointmentStatus;
    private String appointmentContent;
    private LocalDateTime appointmentCreateDate;
    private LocalDateTime appointmentModifyDate;

    @Builder
    public Appointment(Long appointmentId, Long appointmentSenderId, Long appointmentReceiverId, Long possibleDateTimeId, AppointmentStatus appointmentStatus, String appointmentContent, LocalDateTime appointmentCreateDate, LocalDateTime appointmentModifyDate) {
        this.appointmentId = appointmentId;
        this.appointmentSenderId = appointmentSenderId;
        this.appointmentReceiverId = appointmentReceiverId;
        this.possibleDateTimeId = possibleDateTimeId;
        this.appointmentStatus = appointmentStatus;
        this.appointmentContent = appointmentContent;
        this.appointmentCreateDate = appointmentCreateDate;
        this.appointmentModifyDate = appointmentModifyDate;
    }
}
