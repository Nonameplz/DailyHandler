package com.nonameplz.dailyhandler.utils;

import java.security.SecureRandom;
import java.util.UUID;

public class UUIDGenerator {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateUUID() {
        long currentTimeMillis = System.currentTimeMillis();
        int randomInt = secureRandom.nextInt();

        return new UUID(currentTimeMillis, randomInt).toString();
    }
}
