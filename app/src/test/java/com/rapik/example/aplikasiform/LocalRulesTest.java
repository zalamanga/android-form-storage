package com.rapik.example.aplikasiform;

import org.junit.Test;
import static org.junit.Assert.*;

public class LocalRulesTest {
    @Test public void saltedHashesAcceptOnlyTheOriginalPassword() {
        String first = PasswordHasher.hash("local-demo-123");
        String second = PasswordHasher.hash("local-demo-123");
        assertNotEquals(first, second);
        assertTrue(PasswordHasher.verify("local-demo-123", first).matches);
        assertFalse(PasswordHasher.verify("wrong-demo-123", first).matches);
        assertNull(PasswordHasher.verify("local-demo-123", first).replacementHash);
    }

    @Test public void legacyPasswordUpgradesOnlyAfterItMatches() {
        PasswordHasher.Verification correct = PasswordHasher.verify("legacy-demo", "legacy-demo");
        assertTrue(correct.matches);
        assertNotNull(correct.replacementHash);
        assertNotEquals("legacy-demo", correct.replacementHash);
        assertTrue(PasswordHasher.verify("legacy-demo", correct.replacementHash).matches);
        PasswordHasher.Verification wrong = PasswordHasher.verify("wrong-demo", "legacy-demo");
        assertFalse(wrong.matches);
        assertNull(wrong.replacementHash);
    }

    @Test public void corruptOrUnboundedHashInputDoesNotBecomePlaintextLogin() {
        String salt = "00000000000000000000000000000000";
        String hash = "0000000000000000000000000000000000000000000000000000000000000000";
        assertFalse(PasswordHasher.verify("demo", "pbkdf2-sha1$9999999$" + salt + "$" + hash).matches);
        assertFalse(PasswordHasher.verify("demo", "pbkdf2-sha1$120000$bad$bad").matches);
        String malformed = "pbkdf2-sha1$120000$bad$bad";
        assertFalse(PasswordHasher.verify(malformed, malformed).matches);
        assertFalse(PasswordHasher.verify("", "").matches);
        assertFalse(PasswordHasher.verify(null, "old").matches);
    }

    @Test public void formDatesAndSingleLineNamesAreValidatedBeforeRendering() {
        assertNull(FormInput.validate("Siswa Demo", "Class A", "male", "29-02-2024"));
        assertNull(FormInput.validate("Siswa Demo", "Class A", "Female", "31-12-2020"));
        assertNotNull(FormInput.validate("Siswa Demo", "Class A", "male", "29-02-2023"));
        assertNotNull(FormInput.validate("Siswa Demo", "Class A", "male", "01-01-9999"));
        assertNotNull(FormInput.validate("Nama\nPalsu", "Class A", "male", "01-01-2000"));
        assertNotNull(FormInput.validate("Siswa Demo", "Class A", "", "01-01-2000"));
        assertTrue(FormInput.render(" Siswa Demo ", "Class A", "male", "01-01-2000")
                .startsWith("Full Name : Siswa Demo\nClass : Class A\n"));
    }
}
