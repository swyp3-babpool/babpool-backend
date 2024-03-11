package com.swyp3.babpool.infra.auth.service;

import com.swyp3.babpool.infra.auth.AuthPlatform;
import com.swyp3.babpool.infra.auth.domain.Auth;
import com.swyp3.babpool.infra.auth.exception.AuthException;
import com.swyp3.babpool.infra.auth.exception.errorcode.AuthExceptionErrorCode;
import com.swyp3.babpool.infra.auth.kakao.KakaoProvider;
import com.swyp3.babpool.infra.auth.dao.AuthRepository;
import com.swyp3.babpool.infra.auth.kakao.KakaoTokenProvider;
import com.swyp3.babpool.infra.auth.response.AuthMemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final KakaoProvider kakaoProvider;
    private final KakaoTokenProvider kakaoTokenProvider;
    private final AuthRepository authRepository;

    public AuthMemberResponse getUserDataByCode(String code) {
        String idToken = kakaoTokenProvider.getIdTokenFromKakao(code);
        AuthMemberResponse kakaoPlatformMember = kakaoProvider.getKakaoPlatformMember(idToken);

        return kakaoPlatformMember;
    }

    public void save(Auth auth) {
        authRepository.save(auth);
    }

    public AuthPlatform getAuthPlatformByUserId(Long userId) {
        return authRepository.findByUserId(userId).orElseThrow(
                () -> new AuthException(AuthExceptionErrorCode.AUTH_INFO_NOT_FOUND,
                        "해당 사용자의 소셜로그인 정보가 존재하지 않습니다.")
        ).getOauthPlatformName();
    }

    public void socialServiceSignOut(Long userId, AuthPlatform authPlatform) {
        switch (authPlatform) {
            case KAKAO:
                kakaoProvider.kakaoMemberSignOut(userId);
                break;
            default:
                throw new AuthException(AuthExceptionErrorCode.NOT_SUPPORTED_AUTH_PLATFORM,
                        "지원하지 않는 소셜로그인 플랫폼입니다.");
        }
    }

    public void socialServiceDisconnect(Long userId, AuthPlatform authPlatform) {
        switch (authPlatform) {
            case KAKAO:
                Auth auth = authRepository.findByUserId(userId).orElseThrow(
                        () -> new AuthException(AuthExceptionErrorCode.AUTH_INFO_NOT_FOUND,
                                "해당 사용자의 소셜로그인 정보가 존재하지 않습니다.")
                );
                kakaoProvider.kakaoMemberDisconnect(auth.getOauthId());
                break;
            default:
                throw new AuthException(AuthExceptionErrorCode.NOT_SUPPORTED_AUTH_PLATFORM,
                        "지원하지 않는 소셜로그인 플랫폼입니다.");
        }
    }
}