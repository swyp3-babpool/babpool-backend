package com.swyp3.babpool.domain.user.application;

import com.swyp3.babpool.domain.user.api.requset.LoginRequestDTO;
import com.swyp3.babpool.domain.user.api.requset.SignUpRequestDTO;
import com.swyp3.babpool.domain.user.application.response.LoginResponseWithRefreshToken;
import com.swyp3.babpool.domain.user.application.response.MyPageResponse;

public interface UserService {
    LoginResponseWithRefreshToken login(LoginRequestDTO loginRequest);
    LoginResponseWithRefreshToken signUp(SignUpRequestDTO signUpRequest);
    MyPageResponse getMyPage(Long userId);
}
