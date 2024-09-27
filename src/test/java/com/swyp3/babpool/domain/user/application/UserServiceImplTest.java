package com.swyp3.babpool.domain.user.application;

import com.swyp3.babpool.domain.profile.application.ProfileService;
import com.swyp3.babpool.domain.user.dao.UserRepository;
import com.swyp3.babpool.domain.user.domain.User;
import com.swyp3.babpool.domain.user.domain.UserStatus;
import com.swyp3.babpool.domain.user.exception.SignUpException;
import com.swyp3.babpool.domain.user.exception.errorcode.SignUpExceptionErrorCode;
import com.swyp3.babpool.global.tsid.TsidKeyGenerator;
import com.swyp3.babpool.infra.auth.AuthPlatform;
import com.swyp3.babpool.infra.auth.response.AuthMemberResponse;
import com.swyp3.babpool.infra.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private TsidKeyGenerator tsidKeyGenerator;

    @Mock
    private AuthService authService;

    @Mock
    private ProfileService profileService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("최초가입하는 사용자가 소셜로그인을 완료했을 때, PREACTIVE 상태로 데이터베이스에 저장되어야 한다.")
    @Test
    public void testCreateUser() {
        // Given
        AuthPlatform authPlatform = AuthPlatform.GOOGLE;
        AuthMemberResponse authMemberResponse = new AuthMemberResponse("123456", "TestUser", "http://example.com/profile.jpg", "test@example.com");

        Long userId = tsidKeyGenerator.generateTsid();
        when(tsidKeyGenerator.generateTsid()).thenReturn(userId);
        when(userRepository.save(any(User.class))).thenReturn(1);

        // When
        User createdUser = userService.createUser(authPlatform, authMemberResponse);

        // Then
        assertNotNull(createdUser);
        assertEquals(userId, createdUser.getUserId());
        assertEquals("test@example.com", createdUser.getUserEmail());
        assertEquals("TestUser", createdUser.getUserNickName());
        assertEquals(UserStatus.PREACTIVE, createdUser.getUserStatus());

        verify(userRepository).save(argThat(user ->
                user.getUserId().equals(userId) &&
                        user.getUserEmail().equals("test@example.com") &&
                        user.getUserNickName().equals("TestUser") &&
                        user.getUserStatus() == UserStatus.PREACTIVE
        ));
    }

    @DisplayName("사용자 생성 중 DB 저장에 실패하면 SignUpException이 발생한다.")
    @Test
    public void testCreateUser_SaveFailed() {
        // given
        AuthPlatform authPlatform = AuthPlatform.GOOGLE;
        AuthMemberResponse authMemberResponse = new AuthMemberResponse("123456", "TestUser", "http://example.com/profile.jpg", "test@example.com");

        when(tsidKeyGenerator.generateTsid()).thenReturn(1000L);
        when(userRepository.save(any(User.class))).thenReturn(0);

        // when & then
        SignUpException throwedException = assertThrows(SignUpException.class, () -> userService.createUser(authPlatform, authMemberResponse));
        assertEquals("신규 사용자 DB 삽입 실패.", throwedException.getMessage());
        assertEquals(SignUpExceptionErrorCode.SIGNUP_CREATE_FAILED, throwedException.getSignUpExceptionErrorCode());
    }
}