package com.swyp3.babpool.domain.appointment.api.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AppointmentCreateRequest {

    private Long appointmentId; // Not include in request body
    private Long requesterUserId; // Not include in request body
    private Long receiverUserId; // Not include in request body
    private Long targetProfileId;
    private List<Long> possibleTimeIdList;
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
