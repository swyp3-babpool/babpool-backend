package com.swyp3.babpool.domain.review.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ReviewCountByTypeResponse {

    private final Long bestCount;
    private final Long greatCount;
    private final Long badCount;

    @Builder
    public ReviewCountByTypeResponse(Long bestCount, Long greatCount, Long badCount) {
        this.bestCount = bestCount;
        this.greatCount = greatCount;
        this.badCount = badCount;
    }
}
