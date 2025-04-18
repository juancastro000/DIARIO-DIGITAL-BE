package dev.juancastro.digitaldiary.tags;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class TagTest {

    @Test
    void testConstructorWithName() {
        Tag tag = new Tag(TagType.OCIO);
        assertEquals(TagType.OCIO, tag.getName());
        assertNull(tag.getId(), "El ID debería ser null hasta que se persista");
    }

    @Test
    void testDefaultConstructor() {
        Tag tag = new Tag();
        assertNull(tag.getId());
        assertNull(tag.getName());
    }

    @Test
    void testSetterAndGetter() {
        Tag tag = new Tag();
        tag.setName(TagType.DEPORTE);
        assertEquals(TagType.DEPORTE, tag.getName());
    }

    @Test
    void testCustomConstructorWithIdIgnored() {
        Tag tag = new Tag(10L, TagType.FINANZAS);
        assertNull(tag.getId(), "El ID sigue siendo null porque no se asigna en el constructor");
        assertNull(tag.getName(), "El nombre también está null porque no se asigna en el constructor");
    }
}