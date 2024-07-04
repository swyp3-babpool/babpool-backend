package com.swyp3.babpool.domain.appointment.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor
public class AppointmentCreateRequestV1 {


    @Null
    private Long senderUserId;

    @Null
    private Long receiverUserId;

    @Positive(message = "요청 대상 프로필 식별 값이 올바르지 않습니다.")
    private Long targetProfileId;

    @NotEmpty(message = "가능한 시간 리스트가 비어있습니다.")
    private LocalDateTime possibleDateTime;

    @NotBlank(message = "질문 내용이 비어있습니다.")
    private String appointmentContents;

    @Builder
    public AppointmentCreateRequestV1(Long senderUserId, Long receiverUserId, Long targetProfileId, LocalDateTime possibleDateTime, String appointmentContents) {
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.targetProfileId = targetProfileId;
        this.possibleDateTime = possibleDateTime;
        this.appointmentContents = appointmentContents;
    }
}
