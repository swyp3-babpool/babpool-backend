package com.swyp3.babpool.domain.user.application;

import com.swyp3.babpool.domain.appointment.application.response.AppointmentHistoryDoneResponse;
import com.swyp3.babpool.domain.appointment.dao.AppointmentRepository;
import com.swyp3.babpool.domain.profile.application.ProfileService;
import com.swyp3.babpool.domain.profile.domain.Profile;
import com.swyp3.babpool.domain.review.application.ReviewService;
import com.swyp3.babpool.domain.review.application.response.ReviewCountByTypeResponse;
import com.swyp3.babpool.domain.user.application.response.*;
import com.swyp3.babpool.domain.user.dao.ExitInfoRepository;
import com.swyp3.babpool.domain.user.dao.UserRepository;
import com.swyp3.babpool.domain.user.domain.User;
import com.swyp3.babpool.domain.user.domain.UserRole;
import com.swyp3.babpool.domain.user.domain.UserStatus;
import com.swyp3.babpool.domain.user.exception.SignDownException;
import com.swyp3.babpool.domain.user.exception.SignUpException;
import com.swyp3.babpool.domain.user.exception.errorcode.SignDownExceptionErrorCode;
import com.swyp3.babpool.domain.user.exception.errorcode.SignUpExceptionErrorCode;
import com.swyp3.babpool.global.jwt.application.JwtService;
import com.swyp3.babpool.global.jwt.application.response.JwtPairDto;
import com.swyp3.babpool.global.uuid.application.UuidService;
import com.swyp3.babpool.infra.auth.AuthPlatform;
import com.swyp3.babpool.infra.auth.domain.Auth;
import com.swyp3.babpool.domain.user.api.requset.LoginRequestDTO;
import com.swyp3.babpool.domain.user.api.requset.SignUpRequestDTO;
import com.swyp3.babpool.infra.auth.response.AuthMemberResponse;
import com.swyp3.babpool.infra.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final AuthService authService;
    private final UserRepository userRepository;
    private final UuidService uuidService;
    private final JwtService jwtService;
    private final ProfileService profileService;
    private final ReviewService reviewService;
    private final AppointmentRepository appointmentRepository;
    private final ExitInfoRepository exitInfoRepository;

    @Override
    public LoginResponseWithRefreshToken login(LoginRequestDTO loginRequest) {
        AuthMemberResponse kakaoPlatformMember = authService.getUserDataByCode(loginRequest.getCode());
        return generateLoginResponse(AuthPlatform.KAKAO,kakaoPlatformMember);
    }

    @Override
    public LoginResponseWithRefreshToken signUp(SignUpRequestDTO signUpRequest) {
        if(getUserStatus(signUpRequest.getUserUuid()).equals(UserStatus.ACTIVE))
            throw new SignUpException(SignUpExceptionErrorCode.IS_ALREADY_REGISTERED,
                    "이미 데이터베이스에 등록된 사용자이므로 새로운 회원가입을 진행할 수 없습니다.");

        User user = insertUserExtraInfo(signUpRequest);
        return getLoginResponse(user);
    }

    @Transactional
    @Override
    public void signDown(Long userId, String exitReason, String refreshTokenFromCookie) {
        AuthPlatform authPlatformName = authService.getAuthPlatformByUserId(userId);
        authService.socialServiceDisconnect(userId, authPlatformName);

        int updatedRows = userRepository.updateUserStateByUserId(userId, UserStatus.EXIT);
        if(updatedRows!=1){
            throw new SignDownException(SignDownExceptionErrorCode.FAILED_TO_UPDATE_USER_STATE, "회원탈퇴에 실패하였습니다");
        }
        exitInfoRepository.saveExitInfo(userId, exitReason);
        jwtService.logout(refreshTokenFromCookie);
    }

    @Override
    public MyPageResponse getMyPage(Long userId) {
        MyPageUserDaoDto myPageUserDaoDto= userRepository.findMyProfile(userId);
        Profile profile = profileService.getByUserId(userId);
        ReviewCountByTypeResponse reviewCountByType = reviewService.getReviewCountByType(profile.getProfileId());

        List<AppointmentHistoryDoneResponse> doneAppointmentList = appointmentRepository.findDoneAppointmentListByRequesterId(userId);

        // appointmentFixDateTime을 기준으로 내림차순으로 정렬
        Collections.sort(doneAppointmentList, Comparator.comparing(AppointmentHistoryDoneResponse::getAppointmentFixDateTime).reversed());

        // 최신 2개의 AppointmentHistoryDoneResponse 객체만 유지
        if (doneAppointmentList.size() > 2) {
            doneAppointmentList = doneAppointmentList.subList(0, 2);
        }

        MyPageResponse myPageResponse = new MyPageResponse(myPageUserDaoDto, reviewCountByType, doneAppointmentList);
        return myPageResponse;
    }

    @Override
    public UserGradeResponse getUserGrade(Long userId) {
        String userGrade = userRepository.findUserGradeById(userId);
        return new UserGradeResponse(userGrade);
    }

    private UserStatus getUserStatus(String userUuid) {
        Long userId = uuidService.getUserIdByUuid(userUuid);
        User findUser = userRepository.findById(userId);

        return findUser.getUserStatus();
    }

    @Transactional
    public User insertUserExtraInfo(SignUpRequestDTO signUpRequest) {
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
            if(findUser.getUserStatus().equals(UserStatus.PREACTIVE))
                return getLoginResponseNeedSignUp(findUser);// (회원가입이 완료된 경우) 사용자 추가정보 입력 필요
            if(findUser.getUserStatus().equals(UserStatus.EXIT)){
                //회원탈퇴했던 사용자도 사용자 생성부터 시작
                User createdUser = createUser(authPlatform, authMemberResponse);
                return getLoginResponseNeedSignUp(createdUser);
            }
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

    private LoginResponseWithRefreshToken getLoginResponse(User user) {
        String userUuid = String.valueOf(uuidService.getUuidByUserId(user.getUserId()));
        JwtPairDto jwtPair = jwtService.createJwtPair(userUuid, new ArrayList<UserRole>(Arrays.asList(UserRole.USER)));

        String accessToken = jwtPair.getAccessToken();
        String refreshToken = jwtPair.getRefreshToken();

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(userUuid, user.getUserGrade(), accessToken,true);

        return new LoginResponseWithRefreshToken(loginResponseDTO,refreshToken);
    }

    @Transactional
    public User createUser(AuthPlatform authPlatform, AuthMemberResponse authMemberResponse) {
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
