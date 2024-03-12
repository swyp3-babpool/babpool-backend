package com.swyp3.babpool.domain.appointment.api.request;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor
public class AppointmentCreateRequest {

    private Long appointmentId; // Not include in request body
    private Long requesterUserId; // Not include in request body
    private Long receiverUserId; // Not include in request body
    @Positive(message = "요청 대상 프로필 식별 값이 올바르지 않습니다.")
    private Long targetProfileId;
    @NotEmpty(message = "가능한 시간 리스트가 비어있습니다.")
    private List<Long> possibleTimeIdList;
    @NotBlank(message = "질문 내용이 비어있습니다.")
    private String questionContents;
    private Long appointmentRequestId; // Not include in request body

    @Builder
    public AppointmentCreateRequest(Long appointmentId, Long requesterUserId, Long receiverUserId, Long targetProfileId, List<Long> possibleTimeIdList, String questionContents, Long appointmentRequestId) {
        this.appointmentId = appointmentId;
        this.requesterUserId = requesterUserId;
        this.receiverUserId = receiverUserId;
        this.targetProfileId = targetProfileId;
        this.possibleTimeIdList = possibleTimeIdList;
        this.questionContents = questionContents;
        this.appointmentRequestId = appointmentRequestId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    public void setRequesterUserId(Long userId) {
        this.requesterUserId = userId;
    }

    public void setReceiverUserId(Long targetReceiverUserId) {
        this.receiverUserId = targetReceiverUserId;
    }

    public void setAppointmentRequestId(Long appointmentRequestId) {
        this.appointmentRequestId = appointmentRequestId;
    }
}
