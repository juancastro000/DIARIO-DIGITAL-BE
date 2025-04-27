package dev.juancastro.digitaldiary.entries.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.AccessDeniedException;

class AccessDeniedExceptionTest {

    @Test
    @DisplayName("Constructor stores the provided message")
    void testConstructorStoresMessage() {
        String expectedMessage = "Access is denied for this resource";
        AccessDeniedException ex = new AccessDeniedException(expectedMessage);

        assertEquals(expectedMessage, ex.getMessage(),
                "The exception message should match the one provided to the constructor");
        assertNull(ex.getCause(),
                "The cause should be null when no cause is provided");
    }

    @Test
    @DisplayName("Exception is thrown and caught correctly")
    void testThrowingException() {
        String message = "User does not have permission";
        AccessDeniedException thrown = assertThrows(
                AccessDeniedException.class,
                () -> { throw new AccessDeniedException(message); },
                "Expected AccessDeniedException to be thrown"
        );

        assertEquals(message, thrown.getMessage(),
                "Thrown exception should carry the correct message");
    }
}