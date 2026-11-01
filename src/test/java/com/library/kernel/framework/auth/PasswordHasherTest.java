package com.library.kernel.framework.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordHasherTest {

    @Test
    void verifiesCorrectPassword() {
        String hash = PasswordHasher.hash("s3creta");
        assertTrue(PasswordHasher.verify("s3creta", hash));
    }

    @Test
    void rejectsWrongPassword() {
        String hash = PasswordHasher.hash("s3creta");
        assertFalse(PasswordHasher.verify("otra", hash));
    }

    @Test
    void usesRandomSalt() {
        assertNotEquals(PasswordHasher.hash("s3creta"), PasswordHasher.hash("s3creta"));
    }

    @Test
    void rejectsPlainTextOrMalformedStoredValues() {
        assertFalse(PasswordHasher.verify("s3creta", "s3creta"));
        assertFalse(PasswordHasher.verify("s3creta", "pbkdf2$x$y$z"));
        assertFalse(PasswordHasher.verify("s3creta", null));
    }
}
