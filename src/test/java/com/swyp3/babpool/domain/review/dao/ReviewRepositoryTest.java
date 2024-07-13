package com.swyp3.babpool.domain.review.dao;

import com.swyp3.babpool.domain.appointment.dao.AppointmentRepository;
import com.swyp3.babpool.domain.appointment.domain.AppointmentV1;
import com.swyp3.babpool.domain.possibledatetime.dao.PossibleDateTimeRepository;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateInsertDto;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTime;
import com.swyp3.babpool.global.tsid.TsidKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@MybatisTest
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PossibleDateTimeRepository possibleDateTimeRepository;

    private static TsidKeyGenerator tsidKeyGenerator;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void beforeAll(){
        tsidKeyGenerator = new TsidKeyGenerator();
    }

    /**
     * H2 데이터베이스는 isReviewCreateAvailableTime 매퍼에서 사용된 함수를 온전히 지원하지 않는다.
     * DATE_ADD -> TIMESTAMPADD
     */
    @DisplayName("isReviewCreateAvailableTime 매퍼는, 리뷰 생성 가능(약속 일정으로부터 3일이 이내경우) 시간인지 확인한다. 3일 지나면 리뷰를 작성할 수 없어 false를 반환한다.")
    @Test
    void isReviewCreateAvailableTime(){
        //given
        Long targetAppointmentId = 700000000000000007L;

        String query = "SELECT CASE WHEN TIMESTAMPADD(DAY, 3, t_datetime.possible_datetime) > NOW() THEN 1 ELSE 0 END AS result" +
                " FROM t_appointment INNER JOIN t_possible_datetime t_datetime ON t_datetime.possible_datetime_id = t_appointment.possible_datetime_id" +
                " WHERE appointment_id = " + targetAppointmentId;
        //when
        boolean result = jdbcTemplate.queryForObject(query, Boolean.class);

        //then
        log.info("result : {}", result);
        assertFalse(result);
    }

    @DisplayName("isReviewCreateAvailableTime 매퍼는, 리뷰 생성 가능(약속 일정으로부터 3일이 이내경우) 시간인지 확인한다. 3일 이내면 리뷰를 작성할 수 있어 true를 반환한다.")
    @Test
    void isReviewCreateAvailableTime2(){
        //given
        Long senderUserId = 100000000000000001L;
        Long receiverUserId = 100000000000000002L;
        Long possibleDateTimeId = tsidKeyGenerator.generateTsid();
        possibleDateTimeRepository.insertPossibleDateTime(PossibleDateTime.builder()
                .possibleDateTimeId(possibleDateTimeId)
                .userId(senderUserId)
                .possibleDateTime(String.valueOf(LocalDateTime.now()))
                .possibleDateTimeStatus("RESERVED")
                .build());

        Long targetAppointmentId = tsidKeyGenerator.generateTsid();
        appointmentRepository.saveAppointment(AppointmentV1.builder()
                .appointmentId(targetAppointmentId)
                .appointmentSenderId(senderUserId)
                .appointmentReceiverId(receiverUserId)
                        .appointmentQuestion("약속 문의")
                        .appointmentStatus("DONE")
                        .possibleDateTimeId(possibleDateTimeId)
                .build());

        String query = "SELECT CASE WHEN TIMESTAMPADD(DAY, 3, t_datetime.possible_datetime) > NOW() THEN 1 ELSE 0 END AS result" +
                " FROM t_appointment INNER JOIN t_possible_datetime t_datetime ON t_datetime.possible_datetime_id = t_appointment.possible_datetime_id" +
                " WHERE appointment_id = " + targetAppointmentId;
        //when
        boolean result = jdbcTemplate.queryForObject(query, Boolean.class);

        //then
        assertTrue(result);
    }

}