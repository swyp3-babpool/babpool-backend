package com.swyp3.babpool.domain.user.application;

import com.swyp3.babpool.domain.appointment.application.response.AppointmentHistoryDoneResponse;
import com.swyp3.babpool.domain.appointment.dao.AppointmentRepository;
import com.swyp3.babpool.domain.keyword.application.KeywordService;
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
import com.swyp3.babpool.global.tsid.TsidKeyGenerator;
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
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final TsidKeyGenerator tsidKeyGenerator;

    private final AuthService authService;
    private final JwtService jwtService;
    private final ProfileService profileService;
    private final ReviewService reviewService;
    private final KeywordService keywordService;

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final ExitInfoRepository exitInfoRepository;

    @Override
    public LoginResponseWithRefreshToken login(LoginRequestDTO loginRequest, String localhostFlag) {
        AuthMemberResponse kakaoPlatformMember = authService.getUserDataByCode(loginRequest.getCode(), localhostFlag);
        return generateLoginResponse(AuthPlatform.KAKAO,kakaoPlatformMember);
    }

    @Override
    public LoginResponseWithRefreshToken signUp(SignUpRequestDTO signUpRequest) {
        if(getUserStatus(signUpRequest.getUserId()).equals(UserStatus.ACTIVE))
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
        authService.updateOAuthPlatformId(userId);

        profileService.updateProfileActiveFlag(userId, false);

        int updatedRows = userRepository.updateUserStateByUserId(userId, UserStatus.EXIT);
        if(updatedRows!=1){
            throw new SignDownException(SignDownExceptionErrorCode.FAILED_TO_UPDATE_USER_STATE, "회원탈퇴에 실패하였습니다");
        }
        exitInfoRepository.saveExitInfo(tsidKeyGenerator.generateTsid(), userId, exitReason);
        jwtService.logout(refreshTokenFromCookie);
    }

    @Override
    public MyPageResponse getMyPage(Long userId) {
        MyPageUserDto myPageUserDto = userRepository.findMyProfile(userId);
        Profile profile = profileService.getByUserId(userId);
        ReviewCountByTypeResponse reviewCountByType = reviewService.getReviewCountByType(profile.getProfileId());

        List<AppointmentHistoryDoneResponse> doneAppointmentList = appointmentRepository.findDoneAppointmentListByRequesterId(userId);

        // appointmentFixDateTime을 기준으로 내림차순으로 정렬
        Collections.sort(doneAppointmentList, Comparator.comparing(AppointmentHistoryDoneResponse::getAppointmentFixDateTime).reversed());

        // 최신 2개의 AppointmentHistoryDoneResponse 객체만 유지
        if (doneAppointmentList.size() > 2) {
            doneAppointmentList = doneAppointmentList.subList(0, 2);
        }

        MyPageResponse myPageResponse = new MyPageResponse(myPageUserDto, reviewCountByType, doneAppointmentList);
        return myPageResponse;
    }

    @Override
    public UserGradeResponse getUserGrade(Long userId) {
        String userGrade = userRepository.findUserGradeById(userId);
        return new UserGradeResponse(userGrade);
    }

    @Override
    public void updateUserNickNameAndGrade(Long userId, String userNickName, String userGrade) {
        User targetUser = userRepository.findById(userId);
        userRepository.updateUserNickNameAndGrade(userId,
                !targetUser.getUserNickName().equals(userNickName) && StringUtils.hasText(userNickName) ? userNickName : targetUser.getUserNickName(),
                !targetUser.getUserGrade().equals(userGrade) && StringUtils.hasText(userGrade) ? userGrade : targetUser.getUserGrade());

    }

    private UserStatus getUserStatus(Long userId) {
        return userRepository.findById(userId).getUserStatus();
    }

    @Transactional
    public User insertUserExtraInfo(SignUpRequestDTO signUpRequest) {
        Long userId = signUpRequest.getUserId();
        userRepository.updateSignUpInfo(userId, signUpRequest.getUserGrade());

        keywordService.saveUserAndKeywordMapping(userId, signUpRequest.getKeywords());

        return userRepository.findById(userId);
    }

    private LoginResponseWithRefreshToken generateLoginResponse(AuthPlatform authPlatform, AuthMemberResponse authMemberResponse) {
        Long findUserId = userRepository.findUserIdByPlatformAndPlatformId(authPlatform, authMemberResponse.getPlatformId());

        //회원테이블에 id Token 정보가 저장되어있는 경우
        if(findUserId!=null) {
            User findUser = userRepository.findById(findUserId);
            // 회원가입이 완료되었지만, 추가정보 입력이 필요한 경우
            if(findUser.getUserStatus().equals(UserStatus.PREACTIVE))
                return getLoginResponseNeedSignUp(findUser);
            // 회원탈퇴된 사용자인 경우, 사용자 신규 생성
            if(findUser.getUserStatus().equals(UserStatus.EXIT)){
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
        LoginResponse loginResponse = new LoginResponse(user.getUserId(), null,null,false);
        return new LoginResponseWithRefreshToken(loginResponse,null);
    }

    private LoginResponseWithRefreshToken getLoginResponse(User user) {
        JwtPairDto jwtPair = jwtService.createJwtPair(user.getUserId(), new ArrayList<UserRole>(Arrays.asList(user.getUserRole())));

        String accessToken = jwtPair.getAccessToken();
        String refreshToken = jwtPair.getRefreshToken();

        LoginResponse loginResponse = new LoginResponse(user.getUserId(), user.getUserGrade(), accessToken,true);

        return new LoginResponseWithRefreshToken(loginResponse,refreshToken);
    }

    @Transactional
    public User createUser(AuthPlatform authPlatform, AuthMemberResponse authMemberResponse) {
        // 신규 사용자 정보 저장
        Long targetUserId = tsidKeyGenerator.generateTsid();
        User targetUser = User.allArgsBuilder()
                            .userId(targetUserId)
                            .userEmail(authMemberResponse.getEmail())
                            .userNickName(authMemberResponse.getNickname())
                            .allArgsBuild();
        Integer insertedRows = userRepository.save(targetUser);
        if (insertedRows != 1) {
            throw new SignUpException(SignUpExceptionErrorCode.SIGNUP_CREATE_FAILED, "신규 사용자 DB 삽입 실패.");
        }

        // 신규 사용자의 Auth 정보 저장
        authService.createAuth(Auth.builder()
                        .userId(targetUserId)
                        .oauthPlatformName(authPlatform)
                        .oauthPlatformId(authMemberResponse.getPlatformId())
                        .build());

        // 신규 사용자의 프로필 정보 저장
        profileService.createInitProfile(Profile.builder()
                                        .profileId(tsidKeyGenerator.generateTsid())
                                        .userId(targetUserId)
                                        .profileImageUrl(authMemberResponse.getProfile_image())
                                        .profileActiveFlag(false)
                                        .build());

        return targetUser;
    }

}
