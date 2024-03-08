package com.swyp3.babpool.domain.appointment.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AppointmentRefuseRequest {
    @NotNull(message = "appointmentId는 필수 값입니다.")
    private Long appointmentId;
    private String refuseType = "RECEIVER";
    private String refuseMessage;
    private LocalDateTime modifyDate = LocalDateTime.now();
}
