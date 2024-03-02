package com.swyp3.babpool.infra.auth.service;

import com.swyp3.babpool.domain.user.dao.UserRepository;
import com.swyp3.babpool.domain.user.domain.User;
import com.swyp3.babpool.domain.user.domain.UserRole;
import com.swyp3.babpool.global.jwt.application.JwtService;
import com.swyp3.babpool.global.jwt.application.response.JwtPairDto;
import com.swyp3.babpool.global.uuid.application.UuidService;
import com.swyp3.babpool.infra.auth.AuthPlatform;
import com.swyp3.babpool.infra.auth.domain.Auth;
import com.swyp3.babpool.infra.auth.kakao.KakaoMemberProvider;
import com.swyp3.babpool.infra.auth.dao.AuthRepository;
import com.swyp3.babpool.infra.auth.response.AuthMemberResponse;
import com.swyp3.babpool.infra.auth.request.LoginRequestDTO;
import com.swyp3.babpool.infra.auth.response.LoginResponseDTO;
import com.swyp3.babpool.infra.auth.response.LoginResponseWithRefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final KakaoMemberProvider kakaoMemberProvider;
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final JwtService jwtService;
    private final UuidService uuidService;

    public LoginResponseWithRefreshToken kakaoLogin(LoginRequestDTO loginRequest) {
        AuthMemberResponse kakaoPlatformMember = kakaoMemberProvider.getKakaoPlatformMember(loginRequest.getIdToken());
        return generateLoginResponse(AuthPlatform.KAKAO,kakaoPlatformMember);
    }

    private LoginResponseWithRefreshToken generateLoginResponse(AuthPlatform authPlatform, AuthMemberResponse authMemberResponse) {
        Long findUserId = userRepository.findUserIdByPlatformAndPlatformId(authPlatform, authMemberResponse.getPlatformId());

        //회원테이블에 id Token 정보가 저장되어있는 경우
        if(findUserId!=null) {
            User findUser = userRepository.findById(findUserId);
            if(isNeedMoreInfo(findUser))
                return getLoginResponseNeedSignUp(findUser);// (회원가입이 완료된 경우) 사용자 추가정보 입력 필요
            return getLoginResponse(findUser);
        }

        //회원테이블에 아무 정보도 없는 경우
        User createdUser = createUser(authPlatform, authMemberResponse);
        return getLoginResponseNeedSignUp(createdUser);
    }

    private LoginResponseWithRefreshToken getLoginResponseNeedSignUp(User user) {
        String userUuid = String.valueOf(uuidService.getUuidByUserId(user.getUserId()));
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(userUuid, null, false);
        return new LoginResponseWithRefreshToken(loginResponseDTO,null);
    }

    private boolean isNeedMoreInfo(User targetUser){
        if(targetUser.getUserGrade().equals("none")){
            return true;
        }
        return false;
    }

    private LoginResponseWithRefreshToken getLoginResponse(User user) {
        String userUuid = String.valueOf(uuidService.getUuidByUserId(user.getUserId()));
        JwtPairDto jwtPair = jwtService.createJwtPair(userUuid, new ArrayList<UserRole>(Arrays.asList(UserRole.USER)));

        String accessToken = jwtPair.getAccessToken();
        String refreshToken = jwtPair.getRefreshToken();

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(userUuid, accessToken,true);

        return new LoginResponseWithRefreshToken(loginResponseDTO,refreshToken);
    }

    private User createUser(AuthPlatform authPlatform, AuthMemberResponse authMemberResponse) {
        User user = User.builder()
                .email(authMemberResponse.getEmail())
                .nickName(authMemberResponse.getNickname())
                .build();
        userRepository.save(user);
        Long savedId = user.getUserId();

        Auth auth = Auth.createAuth(savedId, authPlatform, authMemberResponse.getPlatformId());
        authRepository.save(auth);

        uuidService.createUuid(savedId);
        return user;
    }
}