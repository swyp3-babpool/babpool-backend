package com.swyp3.babpool.domain.appointment.api.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AppointmentCreateRequest {

    private Long appointmentId;
    private Long requesterUserId;
    private Long targetProfileId;
    private List<Long> possibleTimeIdList;
    private String questionContents;

    @Builder
    public AppointmentCreateRequest(Long appointmentId, Long requesterUserId, Long targetProfileId, List<Long> possibleTimeIdList, String questionContents) {
        this.appointmentId = appointmentId;
        this.requesterUserId = requesterUserId;
        this.targetProfileId = targetProfileId;
        this.possibleTimeIdList = possibleTimeIdList;
        this.questionContents = questionContents;
    }

    public void setRequesterUserId(Long userId) {
        this.requesterUserId = userId;
    }
}