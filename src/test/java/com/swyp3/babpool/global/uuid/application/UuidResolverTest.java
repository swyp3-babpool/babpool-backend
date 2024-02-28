package com.swyp3.babpool.global.uuid.application;

import com.fasterxml.uuid.Generators;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UuidResolverTest {

    /*
    * 참고 링크
    * - https://www.baeldung.com/java-generating-time-based-uuids
    * - https://chanos.tistory.com/entry/MySQL-UUID%EB%A5%BC-%ED%9A%A8%EC%9C%A8%EC%A0%81%EC%9C%BC%EB%A1%9C-%ED%99%9C%EC%9A%A9%ED%95%98%EA%B8%B0-%EC%9C%84%ED%95%9C-%EB%85%B8%EB%A0%A5%EA%B3%BC-%ED%95%9C%EA%B3%84
    * */

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    @DisplayName("UUID byte 크기 확인")
    @Test
    void reduceUuidByteSize(){
        UUID generate = Generators.timeBasedGenerator().generate();
        int length = generate.toString().getBytes().length;
        System.out.println(generate+ " length = " + length);
    }

    @DisplayName("v1 UUID 생성 test")
    @Test
    void generateV1Uuid() {
        for (int i = 0; i < 50; i++) {
            log.info("UUID Version 1: {}", Generators.timeBasedGenerator().generate());
        }
    }

    @DisplayName("v6 UUID 생성 test")
    @Test
    void generateV6Uuid() {
        for (int i = 0; i < 50; i++) {
            log.info("UUID Version 6: {}", Generators.timeBasedReorderedGenerator().generate());
        }
    }

    @DisplayName("v7 UUID 생성 test")
    @Test
    void generateV7Uuid() {
        for (int i = 0; i < 50; i++) {
            log.info("UUID Version 7: {}", Generators.timeBasedEpochGenerator().generate());
        }
    }

    @DisplayName("UUID를 바이트 배열로 변환")
    @Test
    void uuidStringToByte() {
        for (int i = 0; i < 50; i++) {
            UUID uuid = Generators.timeBasedGenerator().generate();
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(uuid.getMostSignificantBits());
            bb.putLong(uuid.getLeastSignificantBits());
            log.info("UUID: {} >> Bytes: {}", uuid, bb.array());
        }
    }

    @DisplayName("UUID를 Bitwise 연산으로 바이트 배열로 변환")
    @Test
    void uuidStringToByteBitwise() {
        for (int i = 0; i < 50; i++) {
            UUID uuid = Generators.timeBasedGenerator().generate();
            long mostSignificantBits = uuid.getMostSignificantBits();
            long leastSignificantBits = uuid.getLeastSignificantBits();
            long result = Math.abs((mostSignificantBits << 32) | (leastSignificantBits & 0xFFFFFFFFL));
            log.info("UUID: {} >> Abs Bytes: {}", uuid, result);
        }
    }

    @DisplayName("바이트 배열로 변환된 UUID를 16진수 문자열로 변환")
    @Test
    void bytesToHex() {

        UUID uuid = Generators.timeBasedGenerator().generate();
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        byte[] result = bb.array();
        log.info("UUID: {} >> Bytes: {}", uuid, result);

        ByteBuffer byteBuffer = ByteBuffer.wrap(result);
        long mostSignificantBitsOrigin = byteBuffer.getLong();
        long leastSignificantBitsOrigin = byteBuffer.getLong();

        UUID originalUUID = new UUID(mostSignificantBitsOrigin, leastSignificantBitsOrigin);
        Assertions.assertThat(uuid).isEqualTo(originalUUID);
    }





}