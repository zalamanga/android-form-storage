package com.rapik.example.aplikasiform;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordHasher {
    private static final String SCHEME = "pbkdf2-sha1";
    private static final int ITERATIONS = 120_000;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordHasher() {}

    public static final class Verification {
        public final boolean matches;
        public final String replacementHash;

        private Verification(boolean matches, String replacementHash) {
            this.matches = matches;
            this.replacementHash = replacementHash;
        }
    }

    public static String hash(String password) {
        if (password == null || password.isEmpty() || password.length() > 128) {
            throw new IllegalArgumentException("Password must contain 1 to 128 characters.");
        }
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return SCHEME + "$" + ITERATIONS + "$" + hex(salt) + "$"
                + hex(derive(password, salt, ITERATIONS));
    }

    public static Verification verify(String password, String stored) {
        if (password == null || password.isEmpty() || password.length() > 128
                || stored == null || stored.isEmpty()) {
            return new Verification(false, null);
        }
        if (!stored.startsWith(SCHEME + "$")) {
            if (stored.startsWith("pbkdf2-")) return new Verification(false, null);
            boolean same = MessageDigest.isEqual(password.getBytes(StandardCharsets.UTF_8),
                    stored.getBytes(StandardCharsets.UTF_8));
            return new Verification(same, same ? hash(password) : null);
        }
        String[] parts = stored.split("\\$", -1);
        if (parts.length != 4 || !parts[1].matches("[0-9]{1,7}")
                || !parts[2].matches("[0-9a-f]{32}") || !parts[3].matches("[0-9a-f]{64}")) {
            return new Verification(false, null);
        }
        int iterations = Integer.parseInt(parts[1]);
        if (iterations < 10_000 || iterations > 1_000_000) {
            return new Verification(false, null);
        }
        boolean same = MessageDigest.isEqual(unhex(parts[3]),
                derive(password, unhex(parts[2]), iterations));
        return new Verification(same, same && iterations < ITERATIONS ? hash(password) : null);
    }

    private static byte[] derive(String password, byte[] salt, int iterations) {
        // SHA-1 PBKDF2 is available on the retained Android API 24 minimum.
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 256);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
                    .generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException error) {
            throw new IllegalStateException("Local password hashing unavailable.", error);
        } finally {
            spec.clearPassword();
        }
    }

    private static String hex(byte[] bytes) {
        StringBuilder text = new StringBuilder(bytes.length * 2);
        for (byte value : bytes) {
            text.append(Character.forDigit((value >>> 4) & 15, 16));
            text.append(Character.forDigit(value & 15, 16));
        }
        return text.toString();
    }

    private static byte[] unhex(String text) {
        byte[] bytes = new byte[text.length() / 2];
        for (int index = 0; index < bytes.length; index++) {
            bytes[index] = (byte) Integer.parseInt(text.substring(index * 2, index * 2 + 2), 16);
        }
        return bytes;
    }
}
