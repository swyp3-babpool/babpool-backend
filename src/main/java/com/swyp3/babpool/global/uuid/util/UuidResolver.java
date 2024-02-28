package com.swyp3.babpool.global.uuid.util;

import com.fasterxml.uuid.Generators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.UUID;

@Slf4j
@Component
public class UuidResolver {

    public UUID generateUuid() {
        UUID uuidV7 = Generators.timeBasedEpochGenerator().generate();
        log.info("UUID Version 7 created : {}", uuidV7);
        return uuidV7;
    }

    public byte[] parseUuidToBytes(UUID targetUuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(targetUuid.getMostSignificantBits());
        bb.putLong(targetUuid.getLeastSignificantBits());
        return bb.array();
    }

    public UUID parseBytesToUuid(byte[] targetUuidBytes) {
        ByteBuffer bb = ByteBuffer.wrap(targetUuidBytes);
        long mostSignificantBits = bb.getLong();
        long leastSignificantBits = bb.getLong();
        return new UUID(mostSignificantBits, leastSignificantBits);
    }

}
