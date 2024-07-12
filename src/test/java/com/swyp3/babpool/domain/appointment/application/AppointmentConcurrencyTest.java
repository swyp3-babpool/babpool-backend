package com.swyp3.babpool.domain.appointment.application;

import com.swyp3.babpool.domain.appointment.api.request.AppointmentCreateRequestV1;
import com.swyp3.babpool.domain.appointment.application.response.AppointmentCreateResponse;
import com.swyp3.babpool.domain.appointment.dao.AppointmentRepository;
import com.swyp3.babpool.domain.appointment.domain.AppointmentV1;
import com.swyp3.babpool.domain.possibledatetime.application.PossibleDateTimeService;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTime;
import com.swyp3.babpool.domain.profile.application.ProfileService;
import com.swyp3.babpool.domain.profile.dao.ProfileRepository;
import com.swyp3.babpool.domain.profile.domain.Profile;
import com.swyp3.babpool.domain.user.dao.UserRepository;
import com.swyp3.babpool.global.uuid.application.UuidService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AppointmentConcurrencyTest {

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;
    @Mock
    private UuidService uuidService;
    @Mock
    private PossibleDateTimeService possibleDateTimeService;
    @Mock
    private ProfileService profileService;

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("1명의 클라이언트만 밥 약속 요청한 경우, 정상적으로 밥 약속을 생성한다.")
    @Test
    void makeAppointmentResolveConcurrency() {
        // given
        AppointmentCreateRequestV1 request = AppointmentCreateRequestV1.builder()
                .appointmentId(null)
                .senderUserId(100000000000000001L)
                .receiverUserId(100000000000000002L)
                .targetProfileId(200000000000000001L)
                .possibleDateTimeId(300000000000000001L)
                .possibleDateTime(LocalDateTime.of(2024, 7, 1, 12, 0))
                .appointmentContents("content1")
                .build();

        Profile profileByProfileId = Profile.builder()
                .profileId(200000000000000001L)
                .userId(100000000000000001L)
                .profileImageUrl("profileImageUrl")
                .profileIntro("profileIntro")
                .profileContents("profileContents")
                .profileContactPhone("profileContactPhone")
                .profileContactChat("profileContactChat")
                .profileActiveFlag(true)
                .build();

        Mockito.when(profileService.getProfileByProfileId(any(Long.class)))
                .thenReturn(profileByProfileId);
        Mockito.when(possibleDateTimeService.throwExceptionIfAppointmentAlreadyAcceptedAtSameTime(any(Long.class), any(Long.class), any(LocalDateTime.class)))
                .thenReturn(PossibleDateTime.builder().possibleDateTimeId(300000000000000001L).build());
        Mockito.when(appointmentRepository.saveAppointment(any(AppointmentV1.class)))
                .thenReturn(1);

        // when
        AppointmentCreateResponse appointmentCreateResponse = appointmentService.makeAppointmentResolveConcurrency(request);

        // then
        log.info("appointmentCreateResponse : {}", appointmentCreateResponse);
        assertNotNull(appointmentCreateResponse);

    }

    @DisplayName("2명의 클라이언트가 동시에 밥 약속 요청한 경우, 더 빨리 접근한 클라이언트의 요청만 밥 약속을 생성한다.")
    @Test
    void makeAppointmentResolveConcurrencyFail() {
        // given
        AppointmentCreateRequestV1 request1 = AppointmentCreateRequestV1.builder()
                .appointmentId(null)
                .senderUserId(100000000000000001L)
                .receiverUserId(100000000000000002L)
                .targetProfileId(200000000000000001L)
                .possibleDateTimeId(300000000000000001L)
                .possibleDateTime(LocalDateTime.of(2024, 7, 1, 12, 0))
                .appointmentContents("content1")
                .build();

        AppointmentCreateRequestV1 request2 = AppointmentCreateRequestV1.builder()
                .appointmentId(null)
                .senderUserId(100000000000000002L)
                .receiverUserId(100000000000000001L)
                .targetProfileId(200000000000000001L)
                .possibleDateTimeId(300000000000000001L)
                .possibleDateTime(LocalDateTime.of(2024, 7, 1, 12, 0))
                .appointmentContents("content2")
                .build();

        // when

    }
}