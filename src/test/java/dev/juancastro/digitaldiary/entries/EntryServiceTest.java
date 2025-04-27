package dev.juancastro.digitaldiary.entries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.juancastro.digitaldiary.tags.Tag;
import dev.juancastro.digitaldiary.tags.TagRepository;
import dev.juancastro.digitaldiary.tags.TagType;
import dev.juancastro.digitaldiary.users.User;
import dev.juancastro.digitaldiary.users.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntryServiceTest {

    @Mock
    private EntryRepository entryRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EntryService entryService;

    private User mockUser;
    private Tag tag1;
    private Tag tag2;
    private Set<Long> tagIds;
    private Set<Tag> mockTags;
    private EntryDto baseDto;

    @BeforeEach
    void setUp() {
        mockUser = new User(1L, "john", "hashedPw", "john@example.com");

        tag1 = new Tag(1L, TagType.PERSONAL);
        tag2 = new Tag(2L, TagType.TRABAJO);
        mockTags = new HashSet<>(Arrays.asList(tag1, tag2));
        tagIds = new HashSet<>();
        tagIds.add(tag1.getId());
        tagIds.add(tag2.getId());

        baseDto = new EntryDto(
            null,
            "contenido base",
            LocalDateTime.of(2025, 4, 19, 12, 0),
            "CALM",
            "3",
            tagIds,
            null
        );
    }

    @Test
    void testCreateEntrySuccess() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(mockUser));
        when(tagRepository.findAllById(tagIds)).thenReturn(new ArrayList<>(mockTags));

        when(entryRepository.save(any(Entry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EntryDto result = entryService.createEntry(baseDto, "john");

        assertNotNull(result);
        assertNull(result.id());
        assertEquals(baseDto.content(), result.content());
        assertEquals(baseDto.date(), result.date());
        assertEquals(baseDto.mood(), result.mood());
        assertEquals(baseDto.productivity(), result.productivity());
        assertEquals(tagIds, result.tagIds());
        assertEquals(mockUser.getId(), result.userId());

        ArgumentCaptor<Entry> captor = ArgumentCaptor.forClass(Entry.class);
        verify(entryRepository).save(captor.capture());
        Entry toSave = captor.getValue();
        assertEquals(baseDto.content(), toSave.getContent());
        assertEquals(mockTags, toSave.getTags());
        assertEquals(mockUser, toSave.getUser());
    }

     @Test
    void testCreateEntryUserNotFound() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        assertThrows(
            RuntimeException.class,
            () -> entryService.createEntry(baseDto, "john")
        );
        verify(tagRepository, never()).findAllById(anySet());
        verify(entryRepository, never()).save(any());
    }

    @Test
    void testGetEntryByIdSuccess() {
        Entry mockEntry = mock(Entry.class);
        when(mockEntry.getId()).thenReturn(5L);
        when(mockEntry.getContent()).thenReturn("cont");
        LocalDateTime now = LocalDateTime.of(2025,4,19,10,0);
        when(mockEntry.getDate()).thenReturn(now);
        when(mockEntry.getMood()).thenReturn("SAD");
        when(mockEntry.getProductivity()).thenReturn("2");
        when(mockEntry.getUser()).thenReturn(mockUser);
        when(mockEntry.getTags()).thenReturn(mockTags);

        when(entryRepository.findById(5L)).thenReturn(Optional.of(mockEntry));

        EntryDto dto = entryService.getEntryById(5L);
        assertNotNull(dto);
        assertEquals(5L, dto.id());
        assertEquals("cont", dto.content());
        assertEquals(now, dto.date());
        assertEquals("SAD", dto.mood());
        assertEquals("2", dto.productivity());
        assertEquals(mockTags.stream().map(Tag::getId).collect(Collectors.toSet()), dto.tagIds());
        assertEquals(mockUser.getId(), dto.userId());
    }

    @Test
    void testGetEntryByIdNotFound() {
        when(entryRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(
            RuntimeException.class,
            () -> entryService.getEntryById(99L)
        );
    }

    @Test
    void testGetAllEntries() {
        Entry e1 = mock(Entry.class);
        Entry e2 = mock(Entry.class);
        when(e1.getId()).thenReturn(10L);
        when(e1.getContent()).thenReturn("a");
        when(e1.getDate()).thenReturn(LocalDateTime.of(2025,4,19,11,0));
        when(e1.getMood()).thenReturn("HAPPY");
        when(e1.getProductivity()).thenReturn("1");
        when(e1.getUser()).thenReturn(mockUser);
        when(e1.getTags()).thenReturn(mockTags);
        when(e2.getId()).thenReturn(20L);
        when(e2.getContent()).thenReturn("b");
        when(e2.getDate()).thenReturn(LocalDateTime.of(2025,4,19,12,0));
        when(e2.getMood()).thenReturn("CALM");
        when(e2.getProductivity()).thenReturn("2");
        when(e2.getUser()).thenReturn(mockUser);
        when(e2.getTags()).thenReturn(Collections.singleton(tag1));

        when(entryRepository.findAll()).thenReturn(Arrays.asList(e1, e2));

        List<EntryDto> all = entryService.getAllEntries();
        assertEquals(2, all.size());
        for (EntryDto dto : all) {
            assertNotNull(dto.content());
        }
    }

    @Test
void testUpdateEntrySuccess() {
    Entry existing = mock(Entry.class);
    when(existing.getId()).thenReturn(7L);
    when(entryRepository.findById(7L)).thenReturn(Optional.of(existing));

    EntryDto updateDto = new EntryDto(
        null,
        "new content",
        LocalDateTime.of(2025, 4, 20, 10, 0),
        "HAPPY",
        "5",
        tagIds,
        null
    );
    doAnswer(inv -> {
        when(existing.getContent()).thenReturn("new content");
        when(existing.getDate()).thenReturn(LocalDateTime.of(2025, 4, 20, 10, 0));
        when(existing.getMood()).thenReturn("HAPPY");
        when(existing.getProductivity()).thenReturn("5");
        when(existing.getTags()).thenReturn(mockTags);
        when(existing.getUser()).thenReturn(mockUser);
        return existing;
    }).when(entryRepository).save(existing);

    when(tagRepository.findAllById(tagIds)).thenReturn(new ArrayList<>(mockTags));

    EntryDto result = entryService.updateEntry(7L, updateDto);

    assertNotNull(result);
    assertEquals("new content", result.content());
    assertEquals(tagIds, result.tagIds());
    assertEquals("HAPPY", result.mood());
    assertEquals("5", result.productivity());
}


    @Test
    void testUpdateEntryNotFound() {
        when(entryRepository.findById(8L)).thenReturn(Optional.empty());
        assertThrows(
            RuntimeException.class,
            () -> entryService.updateEntry(8L, baseDto)
        );
    }

    @Test
    void testDeleteEntrySuccess() {
        when(entryRepository.existsById(3L)).thenReturn(true);
        doNothing().when(entryRepository).deleteById(3L);

        assertDoesNotThrow(() -> entryService.deleteEntry(3L));
        verify(entryRepository).deleteById(3L);
    }

    @Test
    void testDeleteEntryNotFound() {
        when(entryRepository.existsById(4L)).thenReturn(false);
        assertThrows(
            RuntimeException.class,
            () -> entryService.deleteEntry(4L)
        );
    }
}
