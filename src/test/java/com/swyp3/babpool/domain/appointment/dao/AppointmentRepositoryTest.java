package com.swyp3.babpool.domain.appointment.dao;

import com.swyp3.babpool.domain.appointment.domain.Appointment;
import com.swyp3.babpool.domain.appointment.domain.AppointmentStatus;
import com.swyp3.babpool.domain.possibledatetime.dao.PossibleDateTimeRepository;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTime;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTimeStatusType;
import com.swyp3.babpool.domain.user.dao.UserRepository;
import com.swyp3.babpool.domain.user.domain.User;
import com.swyp3.babpool.domain.user.domain.UserRole;
import com.swyp3.babpool.domain.user.domain.UserStatus;
import com.swyp3.babpool.global.tsid.TsidKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@MybatisTest
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PossibleDateTimeRepository possibleDateTimeRepository;

    private TsidKeyGenerator tsidKeyGenerator = new TsidKeyGenerator();
    private Long appointmentId;
    private Long senderUserId;
    private Long receiverUserId;
    private Long possibleDateTimeId;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUpData() {
        appointmentId = tsidKeyGenerator.generateTsid();
        senderUserId = tsidKeyGenerator.generateTsid();
        receiverUserId = tsidKeyGenerator.generateTsid();
        possibleDateTimeId = tsidKeyGenerator.generateTsid();

        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:00:00");
        String formattedDateTime = dateTime.format(formatter);

        userRepository.save(User.allArgsBuilder().userId(senderUserId).userEmail("test1@gmail.com").userNickName("test1")
                .userGrade("FIRST_GRADE").userStatus(UserStatus.ACTIVE).userRole(UserRole.USER).allArgsBuild());
        userRepository.save(User.allArgsBuilder().userId(receiverUserId).userEmail("test2@gmail.com").userNickName("test2")
                .userGrade("FIRST_GRADE").userStatus(UserStatus.ACTIVE).userRole(UserRole.USER).allArgsBuild());
        possibleDateTimeRepository.save(PossibleDateTime.builder().possibleDateTimeId(possibleDateTimeId)
                .userId(receiverUserId).possibleDateTimeStatus(PossibleDateTimeStatusType.AVAILABLE)
                .possibleDateTime(LocalDateTime.parse(formattedDateTime)).build());

    }

    @DisplayName("saveAppointment 매퍼는 Appointment 객체로 신규 약속을 저장한다.")
    @Test
    void saveAppointment() {
        // given
        Long appointmentId = tsidKeyGenerator.generateTsid();
        Appointment targetAppointment = Appointment.builder()
                .appointmentId(appointmentId)
                .appointmentSenderId(senderUserId)
                .appointmentReceiverId(receiverUserId)
                .possibleDateTimeId(possibleDateTimeId)
                .appointmentStatus(AppointmentStatus.WAITING)
                .appointmentContent("test content")
                .appointmentModifyDate(LocalDateTime.now())
                .appointmentCreateDate(LocalDateTime.now())
                .build();

        // when
        int result = appointmentRepository.saveAppointment(targetAppointment);

        // then
        Appointment byAppointmentId = appointmentRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("약속이 존재하지 않습니다."));
        log.info("byAppointmentId: {}", byAppointmentId);
        assertThat(result).isEqualTo(1);
        assertThat(byAppointmentId.getAppointmentId()).isEqualTo(appointmentId);
        assertThat(byAppointmentId.getAppointmentCreateDate()).isNotNull();
    }

    @DisplayName("updateAppointmentStatus 매퍼는 약속의 상태를 변경한다.")
    @Test
    void updateAppointmentStatus() {
        // given
        Appointment targetAppointment = Appointment.builder()
                .appointmentId(appointmentId)
                .appointmentSenderId(senderUserId)
                .appointmentReceiverId(receiverUserId)
                .possibleDateTimeId(possibleDateTimeId)
                .appointmentStatus(AppointmentStatus.WAITING)
                .appointmentContent("test content")
                .build();
        appointmentRepository.saveAppointment(targetAppointment);

        // when
        int result = appointmentRepository.updateAppointmentStatus(appointmentId, AppointmentStatus.ACCEPTED);

        // then
        Appointment byAppointmentId = appointmentRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("약속이 존재하지 않습니다."));
        assertEquals(result, 1);
        assertEquals(byAppointmentId.getAppointmentStatus(), AppointmentStatus.ACCEPTED);
    }

    @DisplayName("deleteAppointmentById 매퍼는 약속을 삭제한다.")
    @Test
    void deleteAppointmentById() {
        // given
        Appointment targetAppointment = Appointment.builder()
                .appointmentId(appointmentId)
                .appointmentSenderId(senderUserId)
                .appointmentReceiverId(receiverUserId)
                .possibleDateTimeId(possibleDateTimeId)
                .appointmentStatus(AppointmentStatus.WAITING)
                .appointmentContent("test content")
                .build();
        appointmentRepository.saveAppointment(targetAppointment);

        // when
        int result = appointmentRepository.deleteAppointmentById(appointmentId);

        // then
        assertThat(result).isEqualTo(1);
        assertThat(appointmentRepository.findByAppointmentId(appointmentId)).isEmpty();
    }

    @DisplayName("updateStatusToExpiredWhereStatusIsWaitingAndAppointmentCreateDatePassedOneDay")
    @Test
    void updateStatusToExpiredWhereStatusIsWaitingAndAppointmentCreateDatePassedOneDay(){
        // given
        LocalDateTime dateTime = LocalDateTime.now().minusDays(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:00:00");
        String formattedDateTime = dateTime.format(formatter);

        Appointment targetAppointment = Appointment.builder()
                .appointmentId(appointmentId)
                .appointmentSenderId(senderUserId)
                .appointmentReceiverId(receiverUserId)
                .possibleDateTimeId(possibleDateTimeId)
                .appointmentStatus(AppointmentStatus.WAITING)
                .appointmentContent("test content")
                .appointmentCreateDate(LocalDateTime.parse(formattedDateTime))
                .build();
        appointmentRepository.saveAppointment(targetAppointment);

        String updateQuery = "update t_appointment set appointment_status = 'EXPIRED' " +
                "where appointment_status = 'WAITING' and TIMESTAMPADD(DAY, 1, appointment_create_date) < CURRENT_TIMESTAMP()";
        // when
        int result = jdbcTemplate.update(updateQuery);

        // then
        assertThat(result).isEqualTo(1);
        assertThat(appointmentRepository.findByAppointmentId(appointmentId).orElse(null).getAppointmentStatus())
                .isEqualTo(AppointmentStatus.EXPIRED);
    }

    @DisplayName("findByAppointmentId 매퍼는 약속 식별값 으로 약속을 조회한다.")
    @Test
    void findByAppointmentId() {
        // given
        Long targetAppointmentId = 700000000000000001L;
        // when
        Appointment byAppointmentId = appointmentRepository.findByAppointmentId(targetAppointmentId)
                .orElseThrow(() -> new IllegalArgumentException("약속이 존재하지 않습니다."));

        // then
        assertThat(byAppointmentId).isNotNull();
        assertThat(byAppointmentId).satisfies(appointment -> {
            assertThat(appointment.getAppointmentId()).isEqualTo(targetAppointmentId);
            assertThat(appointment.getAppointmentSenderId()).isNotNull();
            assertThat(appointment.getAppointmentReceiverId()).isNotNull();
            assertThat(appointment.getPossibleDateTimeId()).isNotNull();
            assertThat(appointment.getAppointmentStatus()).isNotNull();
            assertThat(appointment.getAppointmentContent()).isNotNull();
        });
    }

    @DisplayName("findAllBySenderUserId 매퍼는 송신자 식별값으로 모든 약속 목록을 조회한다.")
    @Test
    void findAllBySenderUserId() {
        // given
        Long targetSenderUserId = 100000000000000001L;
        // when
        List<Appointment> allBySenderUserId = appointmentRepository.findAllBySenderUserId(targetSenderUserId);
        // then
        assertThat(allBySenderUserId).isNotEmpty();
        log.info("allBySenderUserId: {}", allBySenderUserId);
    }
}