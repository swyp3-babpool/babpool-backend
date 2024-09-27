package com.swyp3.babpool.domain.possibledatetime.dao;

import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTime;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTimeStatusType;
import com.swyp3.babpool.global.tsid.TsidKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@MybatisTest
class PossibleDateTimeRepositoryTest {

    @Autowired
    private PossibleDateTimeRepository possibleDateTimeRepository;

    private TsidKeyGenerator tsidKeyGenerator = new TsidKeyGenerator();
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("deletePossibleDateTimeWhereStatusIsNotReserved 매퍼는 삭제 요청 받은 일정 리스트에서 RESERVED 상태가 아닌 일정을 삭제한다.")
    @Test
    void deletePossibleDateTimeWhereStatusIsNotReserved(){
        // given
        Long userId = 100000000000000002L;
        List<LocalDateTime> possibleDateTimeDelList = List.of(
                LocalDateTime.of(2024, 7, 6, 11, 0),
                LocalDateTime.of(2024, 7, 6, 12, 0),
                LocalDateTime.of(2024, 7, 6, 8, 0)
        );

        // when
        possibleDateTimeRepository.deletePossibleDateTimeWhereStatusIsNotReserved(userId, possibleDateTimeDelList);

        // then
        List<PossibleDateTime> possibleDateTimeList = possibleDateTimeRepository.findAllByUserId(userId);
        assertThat(possibleDateTimeList).hasSize(7);
    }

    @DisplayName("deletePossibleDateTimeWhereStatusIsNotReserved 매퍼는 possibleDateTimeDelList가 비어있는 경우, 아무런 작업을 수행하지 않는다.")
    @Test
    void deletePossibleDateTimeWhereStatusIsNotReservedWithEmptyList(){
        // given
        Long userId = 100000000000000002L;
        List<LocalDateTime> possibleDateTimeDelList = List.of();

        // when
        possibleDateTimeRepository.deletePossibleDateTimeWhereStatusIsNotReserved(userId, possibleDateTimeDelList);

        // then
        List<PossibleDateTime> possibleDateTimeList = possibleDateTimeRepository.findAllByUserId(userId);
        assertThat(possibleDateTimeList).hasSize(9);
    }

    @DisplayName("deletePossibleDateTimeWhereStatusIsNotReserved 매퍼는 possibleDateTimeDelList에 존재하지 않는 일정이 존재하면 무시하고 정상동작한다.")
    @Test
    void deletePossibleDateTimeWhereStatusIsNotReservedWithNotExistDateTime(){
        // given
        Long userId = 100000000000000009L;
        List<LocalDateTime> possibleDateTimeAddList = List.of(
                LocalDateTime.of(2024, 7, 6, 11, 0),
                LocalDateTime.of(2024, 7, 6, 12, 0),
                LocalDateTime.of(2024, 7, 6, 8, 0)
        );
        possibleDateTimeRepository.savePossibleDateTimeList(possibleDateTimeAddList.stream()
                .map(dateTime -> PossibleDateTime.builder()
                        .possibleDateTimeId(tsidKeyGenerator.generateTsid())
                        .userId(userId)
                        .possibleDateTime(dateTime)
                        .possibleDateTimeStatus(PossibleDateTimeStatusType.AVAILABLE)
                        .build())
                .toList()
        );
        List<LocalDateTime> possibleDateTimeDelList = List.of(
                LocalDateTime.of(2024, 7, 6, 11, 0),
                LocalDateTime.of(2024, 7, 6, 9, 0),
                LocalDateTime.of(2024, 7, 6, 10, 0)
        );

        // when
        possibleDateTimeRepository.deletePossibleDateTimeWhereStatusIsNotReserved(userId, possibleDateTimeDelList);

        // then
        List<PossibleDateTime> possibleDateTimeList = possibleDateTimeRepository.findAllByUserId(userId);
        log.info("possibleDateTimeList : {}", possibleDateTimeList);
        assertThat(possibleDateTimeList).hasSize(2);
        assertThat(possibleDateTimeList).extracting(PossibleDateTime::getPossibleDateTime)
                .containsAll(List.of(LocalDateTime.of(2024, 7, 6, 12, 0),
                        LocalDateTime.of(2024, 7, 6, 8, 0))
                );
    }

    @DisplayName("savePossibleDateTime 매퍼는 1개 이상의 PossibleDateTime 를 저장한다.")
    @Test
    void savePossibleDateTime(){
        // given
        Long userId = 100000000000000004L;
        PossibleDateTime possibleDateTime1 = PossibleDateTime.builder()
                .possibleDateTimeId(tsidKeyGenerator.generateTsid())
                .userId(userId)
                .possibleDateTime(LocalDateTime.of(2024, 7, 16, 11, 0))
                .build();
        PossibleDateTime possibleDateTime2 = PossibleDateTime.builder()
                .possibleDateTimeId(tsidKeyGenerator.generateTsid())
                .userId(userId)
                .possibleDateTime(LocalDateTime.of(2024, 7, 16, 12, 0))
                .build();

        // when
        possibleDateTimeRepository.savePossibleDateTimeList(List.of(possibleDateTime1, possibleDateTime2));

        // then
        List<PossibleDateTime> allByUserId = possibleDateTimeRepository.findAllByUserId(userId);
        log.info("allByUserId : {}", allByUserId);
        assertThat(allByUserId).hasSize(2);
    }

    // H2 DB 에서는 CAST 함수의 Type 으로 Unsigned Integer를 지원하지 않는다. MySQL에서는 지원하지 않는 BIGINT를 사용해야한다.
    @DisplayName("savePossibleDateTimeListWhereNotExist 매퍼는 중복되는 일정이 존재하지 않는 경우에만 일정을 저장한다.")
    @Test
    void savePossibleDateTimeListWhereNotExist(){
        // given
        Long userId = 100000000000000009L;
        List<LocalDateTime> possibleDateTimeAddList1 = List.of(
                LocalDateTime.of(2024, 7, 6, 11, 0),
                LocalDateTime.of(2024, 7, 6, 12, 0)
        );
        possibleDateTimeRepository.savePossibleDateTimeList(possibleDateTimeAddList1.stream()
                .map(dateTime -> PossibleDateTime.builder()
                        .possibleDateTimeId(tsidKeyGenerator.generateTsid())
                        .userId(userId)
                        .possibleDateTime(dateTime)
                        .possibleDateTimeStatus(PossibleDateTimeStatusType.AVAILABLE)
                        .build())
                .toList());

        List<LocalDateTime> possibleDateTimeAddList2 = List.of(
                LocalDateTime.of(2024, 7, 6, 11, 0),
                LocalDateTime.of(2024, 7, 6, 12, 0),
                LocalDateTime.of(2024, 7, 6, 13, 0)
        );

        // when
        possibleDateTimeRepository.savePossibleDateTimeListWhereNotExistForH2(possibleDateTimeAddList2.stream()
                .map(dateTime -> PossibleDateTime.builder()
                        .possibleDateTimeId(tsidKeyGenerator.generateTsid())
                        .userId(userId)
                        .possibleDateTime(dateTime)
                        .possibleDateTimeStatus(PossibleDateTimeStatusType.AVAILABLE)
                        .build())
                .toList()
        );

        // then
        List<PossibleDateTime> possibleDateTimeList = possibleDateTimeRepository.findAllByUserId(userId);
        log.info("possibleDateTimeList : {}", possibleDateTimeList);
        assertThat(possibleDateTimeList).hasSize(3);
    }

    @DisplayName("findAllByUserId 매퍼는 status와 무관하게 특정 사용자의 모든 가능한 일정을 조회한다.")
    @Test
    void findAllByUserId(){
        // given
        Long userId = 100000000000000002L;

        // when
        List<PossibleDateTime> possibleDateTimeList = possibleDateTimeRepository.findAllByUserId(userId);

        // then
        assertThat(possibleDateTimeList).hasSize(9);
        assertThat(possibleDateTimeList.stream().map(PossibleDateTime::getPossibleDateTimeStatus))
                .containsAll(List.of(PossibleDateTimeStatusType.AVAILABLE, PossibleDateTimeStatusType.RESERVED));
    }

    @DisplayName("findAllByUserIdWhereFromThisMonth 매퍼는 특정 사용자가 활성화한 이달부터의 일정을 모두 조회한다.")
    @Test
    void findAllByUserIdWhereFromThisMonth(){
        // given
        Long userId = 100000000000000002L;
        possibleDateTimeRepository.savePossibleDateTimeList(List.of(PossibleDateTime.builder()
                .possibleDateTimeId(tsidKeyGenerator.generateTsid())
                .possibleDateTime(LocalDateTime.now().minusMonths(1))
                .userId(userId)
                .build()
        ));
        String selectQueryForH2 = "SELECT possible_datetime_id, possible_datetime, possible_datetime_status, user_id" +
                " FROM t_possible_datetime WHERE user_id = " + userId +
                " AND possible_datetime >= FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-01')";

        // when
        List<PossibleDateTime> possibleDateTimeList = jdbcTemplate.query(selectQueryForH2, (rs, rowNum) -> PossibleDateTime.builder()
                .possibleDateTimeId(rs.getLong("possible_datetime_id"))
                .possibleDateTime(rs.getTimestamp("possible_datetime").toLocalDateTime())
                .possibleDateTimeStatus(PossibleDateTimeStatusType.valueOf(rs.getString("possible_datetime_status")))
                .userId(rs.getLong("user_id"))
                .build()
        );

        // then
        assertThat(possibleDateTimeList).isNotNull();
        assertThat(possibleDateTimeList.stream().map(PossibleDateTime::getPossibleDateTime))
                .allMatch(dateTime -> dateTime.isAfter(LocalDateTime.now().minusMonths(1)));
    }

    @DisplayName("findAllByProfileIdWhereFromThisMonth 매퍼는 특정 프로필 식별값으로 이달부터의 일정을 모두 조회한다.")
    @Test
    void findAllByProfileIdWhereFromThisMonth(){
        // given
        Long userId = 100000000000000002L;
        Long profileId = 200000000000000002L;
        possibleDateTimeRepository.savePossibleDateTimeList(List.of(PossibleDateTime.builder()
                .possibleDateTimeId(tsidKeyGenerator.generateTsid())
                .possibleDateTime(LocalDateTime.now().minusMonths(1))
                .userId(userId)
                .build()
        ));
        String selectQueryForH2 = "SELECT possible_datetime_id, possible_datetime, possible_datetime_status, user_id" +
                " FROM t_possible_datetime WHERE user_id IN (SELECT user_id FROM t_profile WHERE profile_id = " +
                profileId + ") AND possible_datetime >= FORMATDATETIME(CURRENT_DATE, 'yyyy-MM-01')" ;

        // when
        List<PossibleDateTime> possibleDateTimeList = jdbcTemplate.query(selectQueryForH2, (rs, rowNum) -> PossibleDateTime.builder()
                .possibleDateTimeId(rs.getLong("possible_datetime_id"))
                .possibleDateTime(rs.getTimestamp("possible_datetime").toLocalDateTime())
                .possibleDateTimeStatus(PossibleDateTimeStatusType.valueOf(rs.getString("possible_datetime_status")))
                .userId(rs.getLong("user_id"))
                .build()
        );

        // then
        assertThat(possibleDateTimeList).isNotNull();
        assertThat(possibleDateTimeList.stream().map(PossibleDateTime::getPossibleDateTime))
                .allMatch(dateTime -> dateTime.isAfter(LocalDateTime.now().minusMonths(1)));
    }

    @DisplayName("updatePossibleDateTimeStatus 매퍼는 일정 식별 값으로 일정 상태를 변경한다.")
    @Test
    void updatePossibleDateTimeStatus(){
            // given
        Long possibleDateTimeId = tsidKeyGenerator.generateTsid();
        Long userId = 100000000000000004L;
        PossibleDateTime possibleDateTime = PossibleDateTime.builder()
                .possibleDateTimeId(possibleDateTimeId)
                .userId(userId)
                .possibleDateTimeStatus(PossibleDateTimeStatusType.AVAILABLE)
                .possibleDateTime(LocalDateTime.now())
                .build();
        possibleDateTimeRepository.savePossibleDateTimeList(List.of(possibleDateTime));

        // when
        int updatedRows = possibleDateTimeRepository.updatePossibleDateTimeStatus(
                possibleDateTimeId, PossibleDateTimeStatusType.RESERVED);

        // then
        PossibleDateTime findById = possibleDateTimeRepository.findById(possibleDateTimeId).orElseThrow(
                () -> new IllegalArgumentException("일정 식별 값으로 조회된 PossibleDateTime 가 존재하지 않습니다.")

        );
        assertThat(updatedRows).isEqualTo(1);
        assertThat(findById.getPossibleDateTimeStatus()).isEqualTo(PossibleDateTimeStatusType.RESERVED);
    }

    @DisplayName("findByProfileIdAndDateTimeForUpdate 매퍼는 FOR UPDATE 절을 사용하여 특정 사용자의 가능한 일정을 조회 후 수정하는 동안 다른 트랜잭션에서 해당 일정을 수정할 수 없다.")
    @Test
    void findByProfileIdAndDateTimeForUpdate() throws InterruptedException {
        // given
        Long possibleDateTimeId = 300000000000000002L;
        Long targetUserId = 100000000000000002L;

        // when
        // 인자생성자로 매개한 카운트가 0이 될 때까지 대기.
        // 메인 테스트 스레드가 레코드를 잠글 때까지 대기할 목적.
        CountDownLatch latch = new CountDownLatch(1);

        // 스레드 생성
        new Thread(() -> {
            // 트랜잭션 생성
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
            transactionTemplate.setPropagationBehavior(Propagation.REQUIRES_NEW.value());
            transactionTemplate.setIsolationLevel(Isolation.READ_COMMITTED.value());

            try {
                transactionTemplate.execute(status -> {
                    // PossibleDateTime 조회 -> 비관적 락 발생
                    PossibleDateTime possibleDateTime = possibleDateTimeRepository.findByUserIdAndDateTimeForUpdate(targetUserId, possibleDateTimeId).orElse(null);
                    assertThat(possibleDateTime).isNotNull();
                    assertThat(possibleDateTime.getPossibleDateTimeId()).isEqualTo(possibleDateTimeId);
                    assertThat(possibleDateTime.getUserId()).isEqualTo(targetUserId);

                    // Signal that the record has been locked
                    // 락이 발생했으니 카운트를 줄여, 메인 테스트 쓰레드를 대기상태에서 해제.
                    latch.countDown();

                    // 잠금을 유지하기 위해 해당 트랜잭션이 종료되지 않도록 5초가량 생성한 스레드를 계속 유지.
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    return null;
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // 스레드 1이 락을 발생시키는데 시간이 걸릴 수 있으니 1초가량 메인 테스트 스레드 대기.
        latch.await(1, TimeUnit.SECONDS);

        // 메인 테스트 스레드에서 락이 걸린 레코드를 조회 시도.
        // 예외가 발생함을 검증
        assertThatThrownBy(() -> {
            // 동일한 자원을 조회 시도. 락이 걸려있으므로 예외 발생.
            possibleDateTimeRepository.findByUserIdAndDateTimeForUpdate(targetUserId, possibleDateTimeId);
        }).isInstanceOf(CannotAcquireLockException.class)
        .hasCauseInstanceOf(org.h2.jdbc.JdbcSQLTimeoutException.class)
        .satisfies(e -> log.info("예외 발생 : {}", e.getMessage()));

    }

    @DisplayName("findByUserIdAndDateTime 매퍼는 사용자 식별 값과 일정 식별 값으로 가능한 일정을 조회한다.")
    @Test
    void findByUserIdAndDateTimeForUpdate(){
        // given
        Long possibleDateTimeId = 300000000000000002L;
        Long targetUserId = 100000000000000002L;

        // when
        PossibleDateTime possibleDateTime = possibleDateTimeRepository.findByUserIdAndDateTimeId(targetUserId, possibleDateTimeId).orElse(null);

        // then
        assertThat(possibleDateTime).isNotNull();
        assertThat(possibleDateTime.getPossibleDateTimeId()).isEqualTo(possibleDateTimeId);
        assertThat(possibleDateTime.getUserId()).isEqualTo(targetUserId);
    }

    @DisplayName("findById 매퍼는 possibledatetime 식별 값으로 일정을 조회한다. 조회에 성공하면 예외가 발생되지 않는다.")
    @Test
    void findById(){
        // given
        Long possibleDateTimeId = 300000000000000002L;

        // when
        assertThatCode(() -> {
            Optional<PossibleDateTime> possibleDateTime = possibleDateTimeRepository.findById(possibleDateTimeId);
            assertThat(possibleDateTime).isPresent();
        }).doesNotThrowAnyException();
    }

    @DisplayName("savePossibleDateTimeList 매퍼는 1개 이상의 PossibleDateTime 를 저장한다.")
    @Test
    void savePossibleDateTimeList(){
        // given
        Long userId = 100000000000000004L;
        PossibleDateTime possibleDateTime1 = PossibleDateTime.builder()
                .possibleDateTimeId(tsidKeyGenerator.generateTsid())
                .userId(userId)
                .possibleDateTime(LocalDateTime.of(2024, 7, 16, 11, 0))
                .build();
        PossibleDateTime possibleDateTime2 = PossibleDateTime.builder()
                .possibleDateTimeId(tsidKeyGenerator.generateTsid())
                .userId(userId)
                .possibleDateTime(LocalDateTime.of(2024, 7, 16, 12, 0))
                .build();

        // when
        possibleDateTimeRepository.savePossibleDateTimeList(List.of(possibleDateTime1, possibleDateTime2));

        // then
        List<PossibleDateTime> allByUserId = possibleDateTimeRepository.findAllByUserId(userId);
        assertThat(allByUserId).hasSize(2);
    }

    @DisplayName("save 매퍼는 단일 PossibleDateTime 를 저장한다.")
    @Test
    void save(){
        // given
        Long userId = 100000000000000004L;
        PossibleDateTime possibleDateTime = PossibleDateTime.builder()
                .possibleDateTimeId(tsidKeyGenerator.generateTsid())
                .userId(userId)
                .possibleDateTime(LocalDateTime.of(2024, 7, 16, 11, 0))
                .build();

        // when
        possibleDateTimeRepository.save(possibleDateTime);

        // then
        List<PossibleDateTime> allByUserId = possibleDateTimeRepository.findAllByUserId(userId);
        assertThat(allByUserId).hasSize(1);
    }



}