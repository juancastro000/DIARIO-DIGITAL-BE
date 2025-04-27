package dev.juancastro.digitaldiary.entries.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.juancastro.digitaldiary.entries.exceptions.ResourceNotFoundException;

class ResourceNotFoundExceptionTest {

    @Test
    @DisplayName("Constructor stores the provided message")
    void testConstructorStoresMessage() {
        String expectedMessage = "Resource with id 123 not found";
        ResourceNotFoundException ex = new ResourceNotFoundException(expectedMessage);

        assertEquals(expectedMessage, ex.getMessage(),
                "The exception message should match the one provided to the constructor");
        assertNull(ex.getCause(),
                "The cause should be null when no cause is provided");
    }

    @Test
    @DisplayName("Exception is thrown and caught correctly")
    void testThrowingException() {
        String message = "User not found";
        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> { throw new ResourceNotFoundException(message); },
                "Expected ResourceNotFoundException to be thrown"
        );

        assertEquals(message, thrown.getMessage(),
                "Thrown exception should carry the correct message");
    }
}