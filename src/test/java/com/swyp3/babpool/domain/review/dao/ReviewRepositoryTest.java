package com.swyp3.babpool.domain.review.dao;

import com.swyp3.babpool.domain.appointment.dao.AppointmentRepository;
import com.swyp3.babpool.domain.appointment.domain.AppointmentV1;
import com.swyp3.babpool.domain.possibledatetime.dao.PossibleDateTimeRepository;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTime;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTimeStatusType;
import com.swyp3.babpool.domain.review.api.request.ReviewCreateRequest;
import com.swyp3.babpool.domain.review.api.request.ReviewUpdateRequest;
import com.swyp3.babpool.domain.review.application.response.ReviewCountByTypeResponse;
import com.swyp3.babpool.domain.review.application.response.ReviewPagingResponse;
import com.swyp3.babpool.domain.review.domain.Review;
import com.swyp3.babpool.domain.review.domain.ReviewRateType;
import com.swyp3.babpool.global.tsid.TsidKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
                " FROM t_appointment appointment INNER JOIN t_possible_datetime t_datetime ON t_datetime.possible_datetime_id = appointment.possible_datetime_id" +
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
        possibleDateTimeRepository.save(PossibleDateTime.builder()
                .possibleDateTimeId(possibleDateTimeId)
                .userId(senderUserId)
                .possibleDateTime(LocalDateTime.now())
                .possibleDateTimeStatus(PossibleDateTimeStatusType.RESERVED)
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

    @DisplayName("countByTypeAndProfileId 매퍼는, 특정 프로필의 리뷰 타입별 리뷰 개수를 반환한다.")
    @Test
    void countReviewByType() {
        //given
        Long profileId = 200000000000000002L;
        // when
        Optional<ReviewCountByTypeResponse> reviewCountByTypeResponse = reviewRepository.countByTypeAndProfileId(profileId);
        // then
        assertThat(reviewCountByTypeResponse).isPresent();
        log.info("reviewCountByTypeResponse : {}", reviewCountByTypeResponse.get());
        assertThat(reviewCountByTypeResponse.get().getBestCount()).isEqualTo(1);
        assertThat(reviewCountByTypeResponse.get().getGreatCount()).isEqualTo(0);
        assertThat(reviewCountByTypeResponse.get().getBadCount()).isEqualTo(0);
    }

    @DisplayName("findByAppointmentId 매퍼는, 특정 약속 식별 값으로 리뷰를 반환한다.")
    @Test
    void findByAppointmentId() {
        //given
        Long appointmentId = 700000000000000007L;
        // when
        Optional<Review> review = reviewRepository.findByAppointmentId(appointmentId);
        // then
        assertThat(review).isPresent();
        assertThat(review.get().getAppointmentId()).isEqualTo(appointmentId);
        log.info("review : {}", review.get());
    }

    @DisplayName("findByReviewId 매퍼는, 특정 리뷰 식별 값으로 리뷰를 반환한다.")
    @Test
    void findByReviewId() {
        //given
        Long reviewId = 800000000000000001L;
        // when
        Optional<Review> review = reviewRepository.findByReviewId(reviewId);
        // then
        assertThat(review).isPresent();
        assertThat(review.get().getReviewId()).isEqualTo(reviewId);
        log.info("review : {}", review.get());
    }

    /**
     * DATE_ADD -> TIMESTAMPADD
     */
    @DisplayName("isReviewUpdateAvailableTime 매퍼는, 특정 리뷰 식별 값으로 리뷰 수정 가능 시간인지 확인한다. 리뷰 생성일로 부터 하루가 지나지 않으면 true를 반환한다.")
    @Test
    void isReviewUpdateAvailableTime() {
        //given
        Long reviewId = 800000000000000001L;
        String query = "SELECT CASE WHEN TIMESTAMPADD(DAY, 1, review_create_date) >= NOW() THEN 1 ELSE 0 END AS result" +
                " FROM t_review WHERE review_id = " + reviewId;
        // when
        boolean result = jdbcTemplate.queryForObject(query, Boolean.class);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("isReviewUpdateAvailableTime 매퍼는, 특정 리뷰 식별 값으로 리뷰 수정 가능 시간인지 확인한다. 리뷰 생성일로 부터 하루가 지나면 false를 반환한다.")
    @Test
    void isReviewUpdateAvailableTime2() {
        //given
        Long reviewId = tsidKeyGenerator.generateTsid();
        LocalDateTime reviewCreateDate = LocalDateTime.now().minusDays(1);
        reviewRepository.save(Review.builder()
                .reviewId(reviewId)
                .appointmentId(700000000000000008L)
                .reviewRate(ReviewRateType.BEST)
                .reviewComment("최고에요")
                .reviewCreateDate(reviewCreateDate)
                .reviewModifyDate(LocalDateTime.now())
                .build());
        String query = "SELECT CASE WHEN TIMESTAMPADD(DAY, 1, review_create_date) >= NOW() THEN 1 ELSE 0 END AS result" +
                " FROM t_review WHERE review_id = " + reviewId;
        // when
        boolean result = jdbcTemplate.queryForObject(query, Boolean.class);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("findAllByProfileIdWithLimit 매퍼는, 특정 프로필의 리뷰 리스트 중 최대 Limit 개수만큼 반환한다.")
    @Test
    void findAllByProfileIdWithLimit() {
        //given
        Long profileId = 200000000000000002L;
        Integer limit = 3;
        reviewRepository.save(Review.builder()
                .reviewId(tsidKeyGenerator.generateTsid())
                .appointmentId(700000000000000008L)
                .reviewRate(ReviewRateType.BEST)
                .reviewComment("최고에요")
                .reviewCreateDate(LocalDateTime.now())
                .reviewModifyDate(LocalDateTime.now())
                .build());
        // when
        List<ReviewPagingResponse> allByProfileIdWithLimit = reviewRepository.findAllByProfileIdWithLimit(profileId, limit);
        // then
        assertThat(allByProfileIdWithLimit).hasSize(2);
        log.info("allByProfileIdWithLimit : {}", allByProfileIdWithLimit);
    }

    @DisplayName("saveReview 매퍼는, ReviewCreateRequest DTO 으로 리뷰를 저장한다.")
    @Test
    void saveReview() {
        //given
        Long reviewId = tsidKeyGenerator.generateTsid();
        Long appointmentId = 700000000000000008L;
        ReviewCreateRequest reviewCreateRequest = ReviewCreateRequest.builder()
                .reviewId(reviewId)
                .appointmentId(appointmentId)
                .reviewRate(ReviewRateType.GREAT)
                .reviewComment("최고에요")
                .build();

        // when
        int result = reviewRepository.saveReview(reviewCreateRequest);

        // then
        reviewRepository.findByReviewId(reviewId).ifPresent(review -> {
            assertThat(review.getAppointmentId()).isEqualTo(appointmentId);
            assertThat(review.getReviewRate()).isEqualTo(ReviewRateType.GREAT);
        });
    }

    @DisplayName("save 매퍼는, Review DTO 으로 리뷰를 저장한다.")
    @Test
    void save() {
        //given
        Long reviewId = tsidKeyGenerator.generateTsid();
        Long appointmentId = 700000000000000008L;
        Review review = Review.builder()
                .reviewId(reviewId)
                .appointmentId(appointmentId)
                .reviewRate(ReviewRateType.BAD)
                .reviewComment("별로에요")
                .reviewCreateDate(LocalDateTime.now())
                .reviewModifyDate(LocalDateTime.now())
                .build();

        // when
        int result = reviewRepository.save(review);

        // then
        reviewRepository.findByReviewId(reviewId).ifPresent(review1 -> {
            assertThat(review1.getAppointmentId()).isEqualTo(appointmentId);
            assertThat(review1.getReviewRate()).isEqualTo(ReviewRateType.BAD);
        });
    }

    @DisplayName("updateReview 매퍼는, ReviewUpdateRequest DTO 으로 리뷰 평가, 코멘트를 수정한다.")
    @Test
    void updateReview() {
        //given
        Long reviewId = 800000000000000001L;
        Long appointmentId = 700000000000000008L;
        ReviewRateType updateRateType = ReviewRateType.GREAT;
        String updateReviewComment = "업데이트";

        ReviewUpdateRequest reviewUpdateRequest = ReviewUpdateRequest.builder()
                .reviewId(reviewId)
                .reviewerUserId(100000000000000001L)
                .appointmentId(appointmentId)
                .reviewRate(updateRateType)
                .reviewComment(updateReviewComment)
                .build();

        // when
        int result = reviewRepository.updateReview(reviewUpdateRequest);

        // then
        reviewRepository.findByReviewId(reviewId).ifPresent(review -> {
            assertThat(review.getReviewRate()).isEqualTo(updateRateType);
            assertThat(review.getReviewComment()).isEqualTo(updateReviewComment);
        });
    }

}