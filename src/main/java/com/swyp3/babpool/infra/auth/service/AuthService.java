package com.swyp3.babpool.infra.auth.service;

import com.swyp3.babpool.domain.user.dao.UserRepository;
import com.swyp3.babpool.domain.user.domain.User;
import com.swyp3.babpool.domain.user.domain.UserRole;
import com.swyp3.babpool.global.util.jwt.JwtTokenizer;
import com.swyp3.babpool.global.util.jwt.application.JwtService;
import com.swyp3.babpool.global.util.jwt.application.response.JwtPairDto;
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

        //회원가입이 되어있는 경우
        if(findUserId!=null) {
            User findUser = userRepository.findById(findUserId);
            return login(findUser);
        }

        //회원가입이 필요한 경우
        return signUp(authPlatform,authMemberResponse);
    }

    private LoginResponseWithRefreshToken signUp(AuthPlatform authPlatform, AuthMemberResponse authMemberResponse) {
        User createUser = createUser(authPlatform, authMemberResponse);
        log.info("회원가입 성공! 추가적인 정보 입력이 필요합니다.");

        return getRedirectToSignUpResponse(createUser);
    }

    private LoginResponseWithRefreshToken getRedirectToSignUpResponse(User user) {
        //회원가입이 되어있지 않으면 JWT 토큰을 반환없이 응답
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO("none", "none", false);
        return new LoginResponseWithRefreshToken(loginResponseDTO,"none");
    }

    private LoginResponseWithRefreshToken login(User findUser) {
        //회원가입은 되어있는데, 추가적인 정보 입력을 하지 않은 경우
        if(findUser.getUserGrade().equals("none")) {
            log.info("회원가입은 되어 있으나, 추가적인 정보 입력이 필요합니다.");
            return getRedirectToSignUpResponse(findUser);
        }
        log.info("로그인에 성공하였습니다.");
        return getLoginResponse(findUser,true);
    }

    private LoginResponseWithRefreshToken getLoginResponse(User createUser, boolean isRegistered) {
        UUID uuid = uuidService.createUuid(createUser.getUserId());
        String userUuid = String.valueOf(uuid);
        JwtPairDto jwtPair = jwtService.createJwtPair(userUuid, new ArrayList<UserRole>(Arrays.asList(UserRole.USER)));

        String accessToken = jwtPair.getAccessToken();
        String refreshToken = jwtPair.getRefreshToken();

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(userUuid, accessToken, isRegistered);

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

        return user;
    }
}

