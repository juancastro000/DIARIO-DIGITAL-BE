package dev.juancastro.digitaldiary.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testEmptyConstructor() {
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getEmail());
    }

    @Test
    public void testConstructorWithUsernameAndPassword() {
        User user = new User("john_doe", "123456");

        assertEquals("john_doe", user.getUsername());
        assertEquals("123456", user.getPassword());
        assertNull(user.getEmail());
    }

    @Test
    public void testConstructorWithAllFields() {
        User user = new User("jane_doe", "abcdef", "jane@example.com");

        assertEquals("jane_doe", user.getUsername());
        assertEquals("abcdef", user.getPassword());
        assertEquals("jane@example.com", user.getEmail());
    }

    @Test
    public void testSettersAndGetters() {
        User user = new User();
        user.setUsername("new_user");
        user.setPassword("new_pass");
        user.setEmail("new@mail.com");

        assertEquals("new_user", user.getUsername());
        assertEquals("new_pass", user.getPassword());
        assertEquals("new@mail.com", user.getEmail());
    }
}