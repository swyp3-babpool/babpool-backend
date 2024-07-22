package com.swyp3.babpool.domain.keyword.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@ToString
@Getter
public class MappingUserKeyword {

    private Long mappingId;
    private Long userId;
    private Long keywordId;

    @Builder
    public MappingUserKeyword(Long mappingId, Long userId, Long keywordId) {
        this.mappingId = mappingId;
        this.userId = userId;
        this.keywordId = keywordId;
    }
}
