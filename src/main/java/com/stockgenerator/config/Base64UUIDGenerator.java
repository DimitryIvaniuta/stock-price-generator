package com.stockgenerator.config;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

@Slf4j
public class Base64UUIDGenerator {

    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        String base64UUID = encodeBase64UUID(uuid);
        log.info("Base64 UUID: {}", base64UUID);
    }

    public static String encodeBase64UUID(UUID uuid) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        return Base64.getUrlEncoder().withoutPadding().encodeToString(byteBuffer.array());
    }

}
