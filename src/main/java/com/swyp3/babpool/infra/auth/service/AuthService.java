package com.swyp3.babpool.infra.auth.service;

import com.swyp3.babpool.domain.user.dao.UserRepository;
import com.swyp3.babpool.domain.user.domain.User;
import com.swyp3.babpool.infra.auth.AuthPlatform;
import com.swyp3.babpool.infra.auth.domain.Auth;
import com.swyp3.babpool.infra.auth.kakao.KakaoMemberProvider;
import com.swyp3.babpool.infra.auth.dao.AuthRepository;
import com.swyp3.babpool.infra.auth.response.AuthMemberResponse;
import com.swyp3.babpool.infra.auth.request.LoginRequestDTO;
import com.swyp3.babpool.infra.auth.response.LoginResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final KakaoMemberProvider kakaoMemberProvider;
    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    public LoginResponseDTO kakaoLogin(LoginRequestDTO loginRequest) {
        //TODO: 들어온 값들 중 NULL 있는지 예외 처리 필요
        AuthMemberResponse kakaoPlatformMember = kakaoMemberProvider.getKakaoPlatformMember(loginRequest.getIdToken());
        return generateLoginResponse(AuthPlatform.KAKAO,kakaoPlatformMember);
    }

    private LoginResponseDTO generateLoginResponse(AuthPlatform authPlatform, AuthMemberResponse authMemberResponse) {
        Long findUserId = userRepository.findUserIdByPlatformAndPlatformId(authPlatform, authMemberResponse.getPlatformId());

        //회원가입이 되어있는 경우
        if(findUserId!=null) {
            User findUser = userRepository.findById(findUserId);
            return login(findUser);
        }

        //회원가입이 필요한 경우
        return signUp(authPlatform,authMemberResponse);
    }

    private LoginResponseDTO signUp(AuthPlatform authPlatform, AuthMemberResponse authMemberResponse) {
        User createUser = createUser(authPlatform, authMemberResponse);
        log.info("회원가입 성공! 추가적인 정보 입력이 필요합니다.");

        return getLoginResponse(createUser,false);
    }

    private LoginResponseDTO login(User findUser) {
        //회원가입은 되어있는데, 추가적인 정보 입력을 하지 않은 경우
        if(findUser.getUserGrade().equals("none")) {
            log.info("회원가입은 되어 있으나, 추가적인 정보 입력이 필요합니다.");
            return getLoginResponse(findUser,false);
        }
        log.info("로그인에 성공하였습니다.");
        return getLoginResponse(findUser,true);
    }

    private LoginResponseDTO getLoginResponse(User createUser, boolean isRegistered) {
        //TODO : 현도님 코드 적용 필요
        String accessToken = "accessToken"; //현도님 코드 사용
        String refreshToken = "refreshToken"; //현도님 코드 사용
        String userUuid = "userUuid"; //후에 현도님 코드 적용 필요

        return new LoginResponseDTO(userUuid, accessToken,isRegistered);
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
