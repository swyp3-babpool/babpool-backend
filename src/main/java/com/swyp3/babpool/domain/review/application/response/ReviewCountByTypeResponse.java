package com.swyp3.babpool.domain.review.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ReviewCountByTypeResponse {

    private final Integer bestCount;
    private final Integer greatCount;
    private final Integer badCount;

    @Builder
    public ReviewCountByTypeResponse(Integer bestCount, Integer greatCount, Integer badCount) {
        this.bestCount = bestCount;
        this.greatCount = greatCount;
        this.badCount = badCount;
    }
}
