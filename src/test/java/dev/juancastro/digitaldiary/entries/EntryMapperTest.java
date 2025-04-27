package dev.juancastro.digitaldiary.entries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import dev.juancastro.digitaldiary.tags.Tag;
import dev.juancastro.digitaldiary.tags.TagType;
import dev.juancastro.digitaldiary.users.User;

class EntryMapperTest {

    @Test
void testToDto() {
    User user = new User(1L, "testuser", "pass", "test@example.com");
    Tag tag1 = new Tag(1L, TagType.PERSONAL);
    Tag tag2 = new Tag(2L, TagType.SALUD);
    Set<Tag> tags = Set.of(tag1, tag2);

    Entry entry = new Entry(
            "Probando",
            LocalDateTime.of(2025, 4, 19, 14, 0),
            "CALM",
            "3",
            user
    );
    entry.setTags(tags);

    assertNotNull(entry.getTags()); // debug extra
    EntryDto dto = EntryMapper.toDto(entry);

    assertNotNull(dto);
    assertEquals(entry.getContent(), dto.content());
    assertEquals(entry.getDate(), dto.date());
    assertEquals(entry.getMood(), dto.mood());
    assertEquals(entry.getProductivity(), dto.productivity());
    assertEquals(user.getId(), dto.userId());
    assertEquals(tags.stream().map(Tag::getId).collect(Collectors.toSet()), dto.tagIds());
}

    @Test
    void testToEntity() {
        Set<Long> tagIds = Set.of(3L, 4L);
        Tag tag3 = new Tag(3L, TagType.FINANZAS);
        Tag tag4 = new Tag(4L, TagType.OCIO);
        Set<Tag> tags = Set.of(tag3, tag4);
        User user = new User(10L, "marta", "1234", "marta@mail.com");

        EntryDto dto = new EntryDto(
                null,
                "Contenido nuevo",
                LocalDateTime.of(2025, 4, 19, 16, 30),
                "HAPPY",
                "4",
                tagIds,
                null
        );

        Entry entry = EntryMapper.toEntity(dto, tags, user);

        assertNotNull(entry);
        assertEquals(dto.content(), entry.getContent());
        assertEquals(dto.date(), entry.getDate());
        assertEquals(dto.mood(), entry.getMood());
        assertEquals(dto.productivity(), entry.getProductivity());
        assertEquals(user, entry.getUser());
        assertEquals(tags, entry.getTags());
    }
}