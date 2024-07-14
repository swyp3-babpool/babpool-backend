package com.swyp3.babpool.domain.possibledatetime.application;

import com.swyp3.babpool.domain.appointment.api.request.AppointmentCreateRequest;
import com.swyp3.babpool.domain.appointment.dao.AppointmentRepository;
import com.swyp3.babpool.domain.appointment.domain.AppointmentV1;
import com.swyp3.babpool.domain.appointment.exception.AppointmentException;
import com.swyp3.babpool.domain.appointment.exception.errorcode.AppointmentErrorCode;
import com.swyp3.babpool.domain.possibledatetime.dao.PossibleDateTimeRepository;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTime;
import com.swyp3.babpool.global.tsid.TsidKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
class PossibleDateTimeServiceImplTest {

    @Autowired
    PossibleDateTimeServiceImpl possibleDateTimeService;

    @Autowired
    PossibleDateTimeRepository possibleDateTimeRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    TsidKeyGenerator tsidKeyGenerator;


    @DisplayName("동시 요청발생 시, 최초의 요청만 성공해 생성된 약속의 개수는 1개여야 한다.")
    @Test
    void throwExceptionIfAppointmentAlreadyAcceptedAtSameTime2() throws InterruptedException {
        // given
        AppointmentCreateRequest appointmentCreateRequest = AppointmentCreateRequest.builder()
                .appointmentId(null)
                .senderUserId(100000000000000001L)
                .receiverUserId(100000000000000002L)
                .targetProfileId(200000000000000002L)
                .possibleDateTimeId(300000000000000002L)
                .possibleDateTime(LocalDateTime.of(2024, 7, 3, 3, 0))
                .appointmentContent("content1")
                .build();

        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    aaa(appointmentCreateRequest);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        // then
        PossibleDateTime possibleDateTime = possibleDateTimeRepository.findAllByProfileId(200000000000000002L).stream()
                .filter(entity -> entity.getPossibleDateTimeStatus().equals("RESERVED"))
                .findFirst().get();
        assertThat(possibleDateTime).isNotNull();

        List<AppointmentV1> allBySenderUserId = appointmentRepository.findAllBySenderUserId(100000000000000001L);
        assertThat(allBySenderUserId.size()).isEqualTo(1);

    }

    @Transactional
    protected void aaa(AppointmentCreateRequest appointmentCreateRequest) {
        PossibleDateTime possibleDateTimeEntity = possibleDateTimeService.throwExceptionIfAppointmentAlreadyAcceptedAtSameTime(
                appointmentCreateRequest.getTargetProfileId(), appointmentCreateRequest.getPossibleDateTimeId(), appointmentCreateRequest.getPossibleDateTime());
        boolean isStatusUpdated = possibleDateTimeService.changeStatusAsReserved(possibleDateTimeEntity.getPossibleDateTimeId());
        if (!isStatusUpdated){
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_CREATE_FAILED, "밥약 가능한 일정 상태 변경에 실패하였습니다.");
        }
        int insertedRows = appointmentRepository.saveAppointment(appointmentCreateRequest.toEntity(tsidKeyGenerator.generateTsid()));
        if (insertedRows == 1){
            log.info("삽입 성공");
        }
    }


}