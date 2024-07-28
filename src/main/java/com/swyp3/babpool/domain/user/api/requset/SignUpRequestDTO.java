package com.swyp3.babpool.domain.user.api.requset;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class SignUpRequestDTO {

    @NotNull(message = "userId 값은 필수입니다.")
    private Long userId;
    @NotNull(message = "userGrade 값은 필수입니다.")
    private String userGrade;
    @NotNull(message = "keyword 값은 필수입니다.")
    private List<Long> keywords;
}
