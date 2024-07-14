package com.swyp3.babpool.domain.keyword.dao.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class KeywordAndUserResponseDto {

    private Long keywordId;
    private Long userId;
    private Long mappingId;
    private String keywordName;
    private String keywordSubject;

    @Builder
    public KeywordAndUserResponseDto(Long keywordId, Long userId, Long mappingId, String keywordName, String keywordSubject) {
        this.keywordId = keywordId;
        this.userId = userId;
        this.mappingId = mappingId;
        this.keywordName = keywordName;
        this.keywordSubject = keywordSubject;
    }
}
