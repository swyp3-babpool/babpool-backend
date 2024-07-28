package com.swyp3.babpool.domain.keyword.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Keyword {

    Long keywordId;
    String keywordSubject;
    String keywordName;

    @Builder
    public Keyword(Long keywordId, String keywordSubject, String keywordName) {
        this.keywordId = keywordId;
        this.keywordSubject = keywordSubject;
        this.keywordName = keywordName;
    }
}
