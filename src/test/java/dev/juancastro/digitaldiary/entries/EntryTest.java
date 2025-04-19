package dev.juancastro.digitaldiary.entries;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import dev.juancastro.digitaldiary.tags.Tag;
import dev.juancastro.digitaldiary.tags.TagType;
import dev.juancastro.digitaldiary.users.User;

import static org.junit.jupiter.api.Assertions.*;

class EntryTest {

    @Test
    void testDefaultConstructorInitializesTagsEmpty() {
        Entry entry = new Entry();
        assertNotNull(entry.getTags(), "Tags no debe ser null");
        assertTrue(entry.getTags().isEmpty(), "Tags debe estar vacío");
        assertNull(entry.getContent(), "Content inicial debe ser null");
        assertNull(entry.getDate(), "Date inicial debe ser null");
        assertNull(entry.getMood(), "Mood inicial debe ser null");
        assertNull(entry.getProductivity(), "Productivity inicial debe ser null");
        assertNull(entry.getUser(), "User inicial debe ser null");
    }

    @Test
    void testParameterizedConstructorAndGetters() {
        User user = new User(1L, "alice", "pw", "alice@example.com");
        LocalDateTime now = LocalDateTime.of(2025, 4, 19, 15, 0);
        Entry entry = new Entry("text", now, "HAPPY", "5", user);

        assertEquals("text", entry.getContent());
        assertEquals(now, entry.getDate());
        assertEquals("HAPPY", entry.getMood());
        assertEquals("5", entry.getProductivity());
        assertEquals(user, entry.getUser());
        assertNotNull(entry.getTags());
        assertTrue(entry.getTags().isEmpty(), "Tags inicial debe estar vacío");
    }

    @Test
    void testSettersAndGetters() {
        Entry entry = new Entry();
        entry.setContent("new content");
        entry.setMood("SAD");
        entry.setProductivity("2");

        User user = new User(2L, "bob", "pw2", "bob@example.com");

        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag(TagType.SALUD));
        entry.setTags(tags);

        assertEquals("new content", entry.getContent());
        assertEquals("SAD", entry.getMood());
        assertEquals("2", entry.getProductivity());
        assertEquals(tags, entry.getTags());
    }

    @Test
    void testPrePersistSetsDateWhenNull() throws Exception {
        Entry entry = new Entry();
        assertNull(entry.getDate(), "Date debe ser null antes de PrePersist");

        Method onCreate = Entry.class.getDeclaredMethod("onCreate");
        onCreate.setAccessible(true);
        onCreate.invoke(entry);

        assertNotNull(entry.getDate(), "Date debe asignarse en onCreate si era null");
        assertTrue(entry.getDate().isBefore(LocalDateTime.now().plusSeconds(1)), "Date asignado debió ser reciente");
    }

    @Test
    void testPrePersistKeepsDateWhenNotNull() throws Exception {
        LocalDateTime fixed = LocalDateTime.of(2025, 1, 1, 0, 0);
        Entry entry = new Entry();
        Method setDate = Entry.class.getDeclaredMethod("setDate", LocalDateTime.class);
        setDate.setAccessible(true);
        setDate.invoke(entry, fixed);

        Method onCreate = Entry.class.getDeclaredMethod("onCreate");
        onCreate.setAccessible(true);
        onCreate.invoke(entry);

        assertEquals(fixed, entry.getDate(), "Date no debe cambiar si ya estaba seteado");
    }
}