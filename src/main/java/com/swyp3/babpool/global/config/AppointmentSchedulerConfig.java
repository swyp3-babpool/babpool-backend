package com.swyp3.babpool.global.config;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import com.swyp3.babpool.domain.appointment.dao.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class AppointmentSchedulerConfig {

    private final AppointmentRepository appointmentRepository;

    // 1시간 마다 업데이트, 초기 1분 딜레이
    @Scheduled(fixedRate = 1000 * 60 * 60, initialDelay = 1000 * 60)
    public void scheduleAppointmentUpdateExpiredStatus() {
        try {
            log.info("scheduleAppointmentUpdateExpiredStatus start alert. Current LocalDateTime {}", LocalDateTime.now());
            int updatedRows = appointmentRepository.updateExpiredStatus();
            log.info("scheduleAppointmentUpdateExpiredStatus end alert. Updated Rows {}", updatedRows);
        }catch (MyBatisSystemException myBatisSystemException) {
            log.error("scheduleAppointmentUpdateExpiredStatus error alert. {}", myBatisSystemException.getMessage());
        }catch (Exception e) {
            log.error("scheduleAppointmentUpdateExpiredStatus error alert. {}", e.getMessage());
        }
    }
}
