package com.swyp3.babpool.domain.possibledatetime.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PossibleDateTimeStatusType {

    RESERVED("RESERVED"),
    AVAILABLE("AVAILABLE")
    ;

    private final String status;
}
