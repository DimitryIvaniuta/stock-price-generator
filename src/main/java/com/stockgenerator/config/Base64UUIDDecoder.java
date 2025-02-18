package com.stockgenerator.config;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

@Slf4j
public class Base64UUIDDecoder {

    public static void main(String[] args) {
        String base64EncodedUUID = "q1Rf3z4cQF-6H8zNyC0VWw"; // Example Base64 UUID

        byte[] bytes = Base64.getUrlDecoder().decode(base64EncodedUUID);

        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long mostSigBits = byteBuffer.getLong();
        long leastSigBits = byteBuffer.getLong();
        UUID uuid = new UUID(mostSigBits, leastSigBits);

        log.info("Decoded UUID: {}", uuid);
    }

}
