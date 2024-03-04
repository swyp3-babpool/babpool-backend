package com.swyp3.babpool.infra.auth.service;

import com.swyp3.babpool.infra.auth.domain.Auth;
import com.swyp3.babpool.infra.auth.kakao.KakaoMemberProvider;
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
    private final KakaoMemberProvider kakaoMemberProvider;
    private final KakaoTokenProvider kakaoTokenProvider;
    private final AuthRepository authRepository;

    public AuthMemberResponse getUserDataByCode(String code) {
        String idToken = kakaoTokenProvider.getIdTokenFromKakao(code);
        AuthMemberResponse kakaoPlatformMember = kakaoMemberProvider.getKakaoPlatformMember(idToken);

        return kakaoPlatformMember;
    }

    public void save(Auth auth) {
        authRepository.save(auth);
    }
}