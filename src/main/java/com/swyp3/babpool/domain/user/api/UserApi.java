package com.swyp3.babpool.domain.user.api;

import com.swyp3.babpool.domain.user.application.response.MyPageResponse;
import com.swyp3.babpool.domain.user.application.UserService;
import com.swyp3.babpool.domain.user.application.response.UserGradeResponse;
import com.swyp3.babpool.global.common.response.ApiResponse;
import com.swyp3.babpool.domain.user.api.requset.LoginRequestDTO;
import com.swyp3.babpool.domain.user.api.requset.SignUpRequestDTO;
import com.swyp3.babpool.domain.user.application.response.LoginResponse;
import com.swyp3.babpool.domain.user.application.response.LoginResponseWithRefreshToken;
import com.swyp3.babpool.global.common.response.CookieProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserApi {
    private final UserService userService;

    /**
     * 로그인 요청 api
     */
    @PostMapping("/sign/in")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequestDTO loginRequest,
                                                            @RequestAttribute(value = "localhostFlag", required = false) String localhostFlag){
        LoginResponseWithRefreshToken loginResponseData = userService.login(loginRequest, localhostFlag);
        Boolean isRegistered = loginResponseData.getLoginResponse().getIsRegistered();

        //로그인 성공한 경우
        if(isRegistered) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, CookieProvider.ofRefreshToken(loginResponseData.getRefreshToken()).toString())
                    .body(ApiResponse.ok(loginResponseData.getLoginResponse()));
        }
        //추가정보 입력이 필요한 경우
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.UNAUTHORIZED, loginResponseData.getLoginResponse()));
    }

    /**
     * 회원가입 요청 api
     */
    @PostMapping("/sign/up")
    public ResponseEntity<ApiResponse<LoginResponse>> signUp(@RequestBody @Valid SignUpRequestDTO signUpRequest){
    LoginResponseWithRefreshToken loginResponseData = userService.signUp(signUpRequest);

    return ResponseEntity
            .status(HttpStatus.OK)
            .header(HttpHeaders.SET_COOKIE, CookieProvider.ofRefreshToken(loginResponseData.getRefreshToken()).toString())
            .body(ApiResponse.ok(loginResponseData.getLoginResponse()));
    }

    /**
     * 마이프로필 조회 api
     */

    @GetMapping("/mypage")
    public ApiResponse<MyPageResponse> getMyPage(@RequestAttribute(value = "userId") Long userId){
        MyPageResponse myPageResponse = userService.getMyPage(userId);
        return ApiResponse.ok(myPageResponse);
    }

    /**
     * 사용자 구분 데이터 조회 api
     */
    @GetMapping("/grade")
    public ApiResponse<UserGradeResponse> getUserGrade(@RequestAttribute(value = "userId") Long userId){
        UserGradeResponse userGradeResponse = userService.getUserGrade(userId);
        return ApiResponse.ok(userGradeResponse);
    }

    /**
     * 회원탈퇴 api
     */
    @PostMapping("/sign/down")
    public ResponseEntity<ApiResponse> signDown(@RequestAttribute(value = "userId") Long userId,
                                                @CookieValue(value = "refreshToken", required = false) String refreshTokenFromCookie,
                                                @RequestBody Map<String, Object> data){
        userService.signDown(userId, data.get("exitReason").toString(), refreshTokenFromCookie);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, CookieProvider.ofRefreshToken("", 0).toString())
                .body(ApiResponse.ok("sign down success"));
    }
}
