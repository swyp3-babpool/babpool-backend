package com.swyp3.babpool.domain.user.domain;

import com.swyp3.babpool.infra.auth.response.AuthMemberResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
public class User {
    private Long userId;
    private String userEmail;
    private UserStatus userStatus;
    private UserRole userRole;
    private String userGrade;
    private String userNickName;
    private LocalDateTime userCreateDate;
    private LocalDateTime userModifyDate;

    // 생성자에 @Builder 적용
    @Builder
    public User(String email, String nickName) {
        this.userEmail= email;
        this.userStatus = UserStatus.PREACTIVE;
        this.userRole = UserRole.USER;
        this.userGrade = "none";
        this.userNickName = nickName;
        this.userCreateDate = LocalDateTime.now();
        this.userModifyDate = LocalDateTime.now();
    }

    @Builder(builderMethodName = "allArgsBuilder", buildMethodName = "allArgsBuild", builderClassName = "allArgsBuilder")
    public User(Long userId, String userEmail, UserStatus userStatus, UserRole userRole, String userGrade, String userNickName, LocalDateTime userCreateDate, LocalDateTime userModifyDate) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userStatus = userStatus;
        this.userRole = userRole;
        this.userGrade = userGrade;
        this.userNickName = userNickName;
        this.userCreateDate = userCreateDate;
        this.userModifyDate = userModifyDate;
    }
}