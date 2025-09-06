package com.chromeckap.backend.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public final class InviteCodeGenerator {
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int DEFAULT_LENGTH = 6;
    private static final int MIN_LENGTH = 4, MAX_LENGTH = 12;

    /**
     * Generates a random invite code with the default length.
     *
     * @return generated invite code
     */
    public static String generate() {
        return generate(DEFAULT_LENGTH);
    }

    /**
     * Generates a random invite code with a custom length.
     *
     * @param length desired length of invite code
     * @return generated invite code
     * @throws IllegalArgumentException if length is less than 4 or length is more than 12
     */
    public static String generate(int length) {
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Invite code length must be between %d and %d characters.", MIN_LENGTH, MAX_LENGTH)
            );
        }

        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int rand = RANDOM.nextInt(CHAR_POOL.length());
            code.append(CHAR_POOL.charAt(rand));
        }
        return code.toString();
    }
}
