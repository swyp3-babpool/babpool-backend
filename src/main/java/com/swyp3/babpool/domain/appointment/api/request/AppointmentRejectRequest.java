package com.swyp3.babpool.domain.appointment.api.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AppointmentRejectRequest {
    @Null
    private Long rejectId;
    @NotNull(message = "appointmentId는 필수 값입니다.")
    private Long appointmentId;
    @NotEmpty(message = "거절 메시지는 필수 값입니다.")
    private String rejectMessage;

    @Builder
    public AppointmentRejectRequest(Long rejectId, Long appointmentId, String rejectMessage) {
        this.rejectId = rejectId;
        this.appointmentId = appointmentId;
        this.rejectMessage = rejectMessage;
    }

    public void setRejectId(Long rejectId) {
        this.rejectId = rejectId;
    }
}
