package com.swyp3.babpool.domain.user.api;

import com.swyp3.babpool.domain.user.application.response.MyPageResponse;
import com.swyp3.babpool.domain.user.application.UserService;
import com.swyp3.babpool.domain.user.application.response.UserGradeResponse;
import com.swyp3.babpool.global.common.response.ApiResponse;
import com.swyp3.babpool.domain.user.api.requset.LoginRequestDTO;
import com.swyp3.babpool.domain.user.api.requset.SignUpRequestDTO;
import com.swyp3.babpool.domain.user.application.response.LoginResponseDTO;
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

    @PostMapping("/sign/in")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody @Valid LoginRequestDTO loginRequest){
//    public ApiResponseWithCookie<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequest){ // 변경 전
        LoginResponseWithRefreshToken loginResponseData = userService.login(loginRequest);
        Boolean isRegistered = loginResponseData.getLoginResponseDTO().getIsRegistered();

        //로그인 성공한 경우 - 변경 전
//        if(isRegistered)
//            return ApiResponseWithCookie.ofRefreshToken(HttpStatus.OK,"로그인에 성공하였습니다",
//                    loginResponseData.getLoginResponseDTO(), loginResponseData.getRefreshToken());
//        //추가정보 입력이 필요한 경우
//        return ApiResponseWithCookie.ofRefreshToken(HttpStatus.UNAUTHORIZED,"추가정보 입력이 필요한 사용자입니다",
//                loginResponseData.getLoginResponseDTO(), loginResponseData.getRefreshToken());

        //로그인 성공한 경우
        if(isRegistered) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, CookieProvider.ofRefreshToken(loginResponseData.getRefreshToken()).toString())
                    .body(ApiResponse.ok(loginResponseData.getLoginResponseDTO()));
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.of(HttpStatus.UNAUTHORIZED, loginResponseData.getLoginResponseDTO()));
    }

//    @PostMapping("/sign/up")
//    public ApiResponseWithCookie<LoginResponseDTO> signUp(@RequestBody @Valid SignUpRequestDTO signUpRequest){
//        LoginResponseWithRefreshToken loginResponseData = userService.signUp(signUpRequest);
//
//        return ApiResponseWithCookie.ofRefreshToken(HttpStatus.OK,"회원가입에 성공하였습니다",
//                loginResponseData.getLoginResponseDTO(), loginResponseData.getRefreshToken());
//    }

    @PostMapping("/sign/up")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> signUp(@RequestBody @Valid SignUpRequestDTO signUpRequest){
    LoginResponseWithRefreshToken loginResponseData = userService.signUp(signUpRequest);

    return ResponseEntity
            .status(HttpStatus.OK)
            .header(HttpHeaders.SET_COOKIE, CookieProvider.ofRefreshToken(loginResponseData.getRefreshToken()).toString())
            .body(ApiResponse.ok(loginResponseData.getLoginResponseDTO()));
}
    @GetMapping("/mypage")
    public ApiResponse<MyPageResponse> getMyPage(@RequestAttribute(value = "userId") Long userId){
        MyPageResponse myPageResponse = userService.getMyPage(userId);
        return ApiResponse.ok(myPageResponse);
    }

    @GetMapping("/grade")
    public ApiResponse<UserGradeResponse> getUserGrade(@RequestAttribute(value = "userId") Long userId){
        UserGradeResponse userGradeResponse = userService.getUserGrade(userId);
        return ApiResponse.ok(userGradeResponse);
    }

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
