package com.swyp3.babpool.domain.appointment.api.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AppointmentAcceptRequest {
    @NotNull(message = "appointmentId는 필수 값입니다.")
    private Long appointmentId;
}
