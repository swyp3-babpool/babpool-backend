package com.swyp3.babpool.infra.auth.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

@Getter
public class SignUpRequestDTO {
    @NotNull(message = "userUuid 값은 필수입니다.")
    private String userUuid;
    @NotNull(message = "userGrade 값은 필수입니다.")
    private String userGrade;
    @NotNull(message = "keyword 값은 필수입니다.")
    private Map<String, Set<String>> keywords;
}
