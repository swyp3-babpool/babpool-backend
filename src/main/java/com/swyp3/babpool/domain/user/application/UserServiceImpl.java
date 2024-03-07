package com.swyp3.babpool.domain.user.application;

import com.swyp3.babpool.domain.profile.application.ProfileService;
import com.swyp3.babpool.domain.profile.domain.Profile;
import com.swyp3.babpool.domain.user.application.response.MyPageResponse;
import com.swyp3.babpool.domain.user.application.response.MyPageUserDaoDto;
import com.swyp3.babpool.domain.user.dao.UserRepository;
import com.swyp3.babpool.domain.user.domain.User;
import com.swyp3.babpool.domain.user.domain.UserRole;
import com.swyp3.babpool.domain.user.exception.SignUpException;
import com.swyp3.babpool.domain.user.exception.errorcode.SignUpExceptionErrorCode;
import com.swyp3.babpool.global.jwt.application.JwtService;
import com.swyp3.babpool.global.jwt.application.response.JwtPairDto;
import com.swyp3.babpool.global.uuid.application.UuidService;
import com.swyp3.babpool.infra.auth.AuthPlatform;
import com.swyp3.babpool.infra.auth.domain.Auth;
import com.swyp3.babpool.domain.user.api.requset.LoginRequestDTO;
import com.swyp3.babpool.domain.user.api.requset.SignUpRequestDTO;
import com.swyp3.babpool.infra.auth.response.AuthMemberResponse;
import com.swyp3.babpool.domain.user.application.response.LoginResponseDTO;
import com.swyp3.babpool.domain.user.application.response.LoginResponseWithRefreshToken;
import com.swyp3.babpool.infra.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final AuthService authService;
    private final UserRepository userRepository;
    private final UuidService uuidService;
    private final JwtService jwtService;
    private final ProfileService profileService;

    public LoginResponseWithRefreshToken login(LoginRequestDTO loginRequest) {
        AuthMemberResponse kakaoPlatformMember = authService.getUserDataByCode(loginRequest.getCode());
        return generateLoginResponse(AuthPlatform.KAKAO,kakaoPlatformMember);
    }

    public LoginResponseWithRefreshToken signUp(SignUpRequestDTO signUpRequest) {
        if(isAlreadyRegisteredUser(signUpRequest.getUserUuid()))
            throw new SignUpException(SignUpExceptionErrorCode.IS_ALREADY_REGISTERED,
                    "이미 데이터베이스에 등록된 사용자이므로 새로운 회원가입을 진행할 수 없습니다.");
        User user = insertUserExtraInfo(signUpRequest);
        return getLoginResponse(user);
    }

    @Override
    public MyPageResponse getMyPage(Long userId) {
        MyPageUserDaoDto myPageUserDaoDto= userRepository.findMyProfile(userId);
        //TODO: 밥약 히스토리와 후기 데이터 추가 필요
        MyPageResponse myPageResponse = new MyPageResponse(myPageUserDaoDto, null, null);
        return myPageResponse;
    }

    private boolean isAlreadyRegisteredUser(String userUuid) {
        Long userId = uuidService.getUserIdByUuid(userUuid);
        User findUser = userRepository.findById(userId);

        return !findUser.getUserGrade().equals("none");
    }

    private User insertUserExtraInfo(SignUpRequestDTO signUpRequest) {
        Long userId = uuidService.getUserIdByUuid(signUpRequest.getUserUuid());
        userRepository.updateSignUpInfo(userId, signUpRequest.getUserGrade());

        for (Long keywordId : signUpRequest.getKeywords()) {
            userRepository.saveKeyword(userId, keywordId);
        }

        return userRepository.findById(userId);
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
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(userUuid, null,null,false);
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

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(userUuid, user.getUserGrade(), accessToken,true);

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
        authService.save(auth);

        Profile profile = Profile.builder()
                .userId(savedId)
                .profileImageUrl(authMemberResponse.getProfile_image())
                .profileActiveFlag(false)
                .build();
        profileService.saveProfile(profile);

        uuidService.createUuid(savedId);
        return user;
    }
}
