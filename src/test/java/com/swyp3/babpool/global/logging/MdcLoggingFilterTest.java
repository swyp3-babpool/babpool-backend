package com.swyp3.babpool.global.logging;

import com.fasterxml.uuid.Generators;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MdcLoggingFilterTest {

    @DisplayName("UUID 생성 테스트_getMostSignificantBits()")
    @RepeatedTest(10)
    void generateUUIDAndParseMostSignificantBits() {
        // given
        // when
        String uuidVersion4 = UUID.randomUUID().toString().substring(0, 8);
        // then
        System.out.println("uuidVersion4 = " + uuidVersion4);
    }

}