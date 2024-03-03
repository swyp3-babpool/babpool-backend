package com.swyp3.babpool.global.common.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;

@ToString
@Getter
public class PagingRequestList<T> {

    private T condition;
    private Pageable pageable;

    @Builder
    public PagingRequestList(T condition, Pageable pageable) {
        this.condition = condition;
        this.pageable = pageable;
    }
}
