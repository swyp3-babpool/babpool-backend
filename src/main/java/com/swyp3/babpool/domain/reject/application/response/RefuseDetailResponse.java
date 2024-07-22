package com.swyp3.babpool.domain.reject.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class RefuseDetailResponse {

    private String receiverNickName;
    private String receiverProfileImage;
    private String receiverGrade;
    private String receiverProfileIntro;
    private String[] receiverKeywords;
    private String refuseMessage;

    @Builder
    public RefuseDetailResponse(String receiverNickName, String receiverProfileImage, String receiverGrade, String receiverProfileIntro, String[] receiverKeywords, String refuseMessage) {
        this.receiverNickName = receiverNickName;
        this.receiverProfileImage = receiverProfileImage;
        this.receiverGrade = receiverGrade;
        this.receiverProfileIntro = receiverProfileIntro;
        this.receiverKeywords = receiverKeywords;
        this.refuseMessage = refuseMessage;
    }
}
