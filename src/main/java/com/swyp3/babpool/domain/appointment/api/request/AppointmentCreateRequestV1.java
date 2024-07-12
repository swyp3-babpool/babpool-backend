package com.swyp3.babpool.domain.appointment.api.request;

import com.swyp3.babpool.domain.appointment.domain.Appointment;
import com.swyp3.babpool.domain.appointment.domain.AppointmentV1;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
public class AppointmentCreateRequestV1 {

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

    @NotEmpty(message = "가능한 시간 리스트가 비어있습니다.")
    private LocalDateTime possibleDateTime;

    @NotBlank(message = "질문 내용이 비어있습니다.")
    private String appointmentContents;

    @Builder
    public AppointmentCreateRequestV1(Long appointmentId, Long senderUserId, Long receiverUserId, Long targetProfileId, Long possibleDateTimeId, LocalDateTime possibleDateTime, String appointmentContents) {
        this.appointmentId = appointmentId;
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.targetProfileId = targetProfileId;
        this.possibleDateTimeId = possibleDateTimeId;
        this.possibleDateTime = possibleDateTime;
        this.appointmentContents = appointmentContents;
    }

    public AppointmentV1 toEntity(Long appointmentId) {
        return AppointmentV1.builder()
                .appointmentId(appointmentId)
                .appointmentSenderId(senderUserId)
                .appointmentReceiverId(receiverUserId)
                .possibleDateTimeId(possibleDateTimeId)
                .appointmentQuestion(appointmentContents)
                .build();
    }

}
