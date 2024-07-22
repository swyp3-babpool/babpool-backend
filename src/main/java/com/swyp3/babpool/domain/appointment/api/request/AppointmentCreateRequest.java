package com.swyp3.babpool.domain.appointment.api.request;

import com.swyp3.babpool.domain.appointment.domain.Appointment;
import com.swyp3.babpool.domain.appointment.domain.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
public class AppointmentCreateRequest {

    @Schema(description = "밥약 식별 값", example = "null", nullable = true)
    @Setter
    @Null
    private Long appointmentId;

    @Schema(description = "밥약 송신자 식별 값", example = "null", nullable = true)
    @Setter
    @Null
    private Long senderUserId;

    @Schema(description = "밥약 수신자 식별 값", example = "null", nullable = true)
    @Setter
    @Null
    private Long receiverUserId;

    @Schema(description = "밥약 수신자 프로필 식별 값", example = "222222222222222222")
    @Positive(message = "요청 대상 프로필 식별 값이 올바르지 않습니다.")
    private Long targetProfileId;

    @Schema(description = "일정 식별 값", example = "null", nullable = true)
    @Setter
    @Null
    private Long possibleDateTimeId;

    @Schema(description = "가능한 일정 값", example = "2024-07-12T03:00:00")
    @NotNull(message = "가능한 일정 값이 비어있습니다.")
    private LocalDateTime possibleDateTime;

    @Schema(description = "질문 내용", example = "밥약 가능한가요?")
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

    public Appointment toEntity() {
        return Appointment.builder()
                .appointmentId(appointmentId)
                .appointmentSenderId(senderUserId)
                .appointmentReceiverId(receiverUserId)
                .possibleDateTimeId(possibleDateTimeId)
                .appointmentStatus(AppointmentStatus.WAITING)
                .appointmentContent(appointmentContent)
                .appointmentCreateDate(LocalDateTime.now())
                .appointmentModifyDate(LocalDateTime.now())
                .build();
    }

}
