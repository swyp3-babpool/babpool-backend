package com.swyp3.babpool.domain.user.domain;

import com.swyp3.babpool.infra.auth.response.AuthMemberResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    //생성 메서드
    public static User createUser(AuthMemberResponse authMemberResponse) {
        User user = new User();

        user.userEmail= authMemberResponse.getEmail();
        user.userStatus = UserStatus.ACTIVE;
        user.userRole = UserRole.USER;
        user.userGrade = "none";
        user.userNickName = authMemberResponse.getNickname();
        user.userCreateDate = LocalDateTime.now();
        user.userModifyDate = LocalDateTime.now();

        return user;
    }
}