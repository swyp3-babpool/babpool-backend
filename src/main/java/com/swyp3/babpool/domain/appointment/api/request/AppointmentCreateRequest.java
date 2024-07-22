package com.swyp3.babpool.domain.appointment.api.request;

import com.swyp3.babpool.domain.appointment.domain.Appointment;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
public class AppointmentCreateRequest {

    @Setter
    @Null
    private Long appointmentId;

    @Setter
    @Null
    private Long senderUserId;

    @Setter
    @Null
    private Long receiverUserId;

    @Positive(message = "요청 대상 프로필 식별 값이 올바르지 않습니다.")
    private Long targetProfileId;

    @Positive(message = "가능한 일정 식별 값이 올바르지 않습니다.")
    private Long possibleDateTimeId;

    @NotNull(message = "가능한 일정 값이 비어있습니다.")
    private LocalDateTime possibleDateTime;

    @NotBlank(message = "질문 내용이 비어있습니다.")
    private String appointmentContent;

    @Builder
    public AppointmentCreateRequest(Long appointmentId, Long senderUserId, Long receiverUserId, Long targetProfileId, Long possibleDateTimeId, LocalDateTime possibleDateTime, String appointmentContent) {
        this.appointmentId = appointmentId;
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.targetProfileId = targetProfileId;
        this.possibleDateTimeId = possibleDateTimeId;
        this.possibleDateTime = possibleDateTime;
        this.appointmentContent = appointmentContent;
    }

    public Appointment toEntity(Long appointmentId) {
        return Appointment.builder()
                .appointmentId(appointmentId)
                .appointmentSenderId(senderUserId)
                .appointmentReceiverId(receiverUserId)
                .possibleDateTimeId(possibleDateTimeId)
                .appointmentContent(appointmentContent)
                .build();
    }

}
