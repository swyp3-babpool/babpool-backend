package com.swyp3.babpool.domain.user.application;

import com.swyp3.babpool.domain.user.application.requset.LoginRequestDTO;
import com.swyp3.babpool.domain.user.application.requset.SignUpRequestDTO;
import com.swyp3.babpool.domain.user.application.response.LoginResponseWithRefreshToken;

public interface UserService {
    public LoginResponseWithRefreshToken login(LoginRequestDTO loginRequest);
    public LoginResponseWithRefreshToken signUp(SignUpRequestDTO signUpRequest);
}
