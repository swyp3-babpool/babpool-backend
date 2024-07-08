package com.swyp3.babpool.domain.user.application;

import com.swyp3.babpool.domain.user.api.requset.LoginRequestDTO;
import com.swyp3.babpool.domain.user.api.requset.SignUpRequestDTO;
import com.swyp3.babpool.domain.user.application.response.LoginResponseWithRefreshToken;
import com.swyp3.babpool.domain.user.application.response.MyPageResponse;
import com.swyp3.babpool.domain.user.application.response.UserGradeResponse;

public interface UserService {
    LoginResponseWithRefreshToken login(LoginRequestDTO loginRequest, String localhostFlag);
    LoginResponseWithRefreshToken signUp(SignUpRequestDTO signUpRequest);

    void signDown(Long userId, String exitReason, String refreshTokenFromCookie);

    MyPageResponse getMyPage(Long userId);

    UserGradeResponse getUserGrade(Long userId);
}
