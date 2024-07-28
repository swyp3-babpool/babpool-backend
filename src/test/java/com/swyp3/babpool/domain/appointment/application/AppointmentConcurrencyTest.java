package com.swyp3.babpool.domain.appointment.application;

import com.swyp3.babpool.domain.appointment.api.request.AppointmentCreateRequest;
import com.swyp3.babpool.domain.appointment.dao.AppointmentRepository;
import com.swyp3.babpool.domain.possibledatetime.application.PossibleDateTimeService;
import com.swyp3.babpool.domain.profile.application.ProfileService;
import com.swyp3.babpool.domain.profile.dao.ProfileRepository;
import com.swyp3.babpool.domain.user.dao.UserRepository;
import com.swyp3.babpool.global.uuid.application.UuidService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;

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

    @DisplayName("2명의 클라이언트가 동시에 밥 약속 요청한 경우, 더 빨리 접근한 클라이언트의 요청만 밥 약속을 생성한다.")
    @Test
    void makeAppointmentResolveConcurrencyFail() {
        // given
        AppointmentCreateRequest request1 = AppointmentCreateRequest.builder()
                .appointmentId(null)
                .senderUserId(100000000000000001L)
                .receiverUserId(100000000000000002L)
                .targetProfileId(200000000000000001L)
                .possibleDateTimeId(300000000000000001L)
                .possibleDateTime(LocalDateTime.of(2024, 7, 1, 12, 0))
                .appointmentContent("content1")
                .build();

        AppointmentCreateRequest request2 = AppointmentCreateRequest.builder()
                .appointmentId(null)
                .senderUserId(100000000000000002L)
                .receiverUserId(100000000000000001L)
                .targetProfileId(200000000000000001L)
                .possibleDateTimeId(300000000000000001L)
                .possibleDateTime(LocalDateTime.of(2024, 7, 1, 12, 0))
                .appointmentContent("content2")
                .build();

        // when

    }
}