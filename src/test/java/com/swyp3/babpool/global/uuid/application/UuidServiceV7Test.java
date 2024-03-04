package com.swyp3.babpool.global.uuid.application;

import com.swyp3.babpool.global.uuid.dao.UserUuidRepository;
import com.swyp3.babpool.global.uuid.domain.UserUuid;
import com.swyp3.babpool.global.uuid.util.UuidResolver;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@MybatisTest
class UuidServiceV7Test {

    private UuidServiceV7 uuidServiceV7;

    @Autowired
    private UserUuidRepository userUuidRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into t_user_account(user_id, user_email,user_status,user_role,user_grade,user_nick_name,user_create_date,user_modify_date)\n" +
                "        values (1, 'test@test.com','active','user','first','test',NOW(),NOW())");
        uuidServiceV7 = new UuidServiceV7(new UuidResolver(), userUuidRepository);
    }

    @Test
    void createUuid() {
        // given
        Long userId = 1L;
        UuidResolver uuidResolver = new UuidResolver();
        // when
        UUID createdUuid = uuidServiceV7.createUuid(userId);
        // then
        log.info("UUID : {}", createdUuid);
        assertNotNull(createdUuid);
        Optional<UserUuid> insertedUserUuid = userUuidRepository.findByUserUuIdBytes(uuidResolver.parseUuidToBytes(createdUuid));
        log.info("insertedUserUuid : {}", insertedUserUuid.toString());
        Assertions.assertThat(insertedUserUuid.get().getUserId()).isEqualTo(userId);
        Assertions.assertThat(uuidResolver.parseBytesToUuid(insertedUserUuid.get().getUserUuid())).isEqualTo(createdUuid);
        log.info("insertedUserUuid parseBytes to UUID : {}", uuidResolver.parseBytesToUuid(insertedUserUuid.get().getUserUuid()));

    }

    @Test
    void getUuidByUserId() {
        // given
        Long userId = 1L;
        UUID createdUuid = uuidServiceV7.createUuid(userId);
        // when
        UUID resultUuid = uuidServiceV7.getUuidByUserId(userId);
        // then
        log.info("UUID : {}", resultUuid);
        assertNotNull(resultUuid);
        Assertions.assertThat(resultUuid).isEqualTo(createdUuid);
    }

    @Test
    void getUserIdByUuid() {
        // given
        Long userId = 1L;
        UUID createdUuid = uuidServiceV7.createUuid(userId);
        log.info("createdUuid : {}", createdUuid);
        // when
        Long resultUserId = uuidServiceV7.getUserIdByUuid(createdUuid.toString());

        // then
        log.info("resultUserId : {}", resultUserId);
        assertNotNull(resultUserId);
        Assertions.assertThat(resultUserId).isEqualTo(userId);
    }

}