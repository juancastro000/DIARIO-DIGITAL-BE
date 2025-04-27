package dev.juancastro.digitaldiary.config;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.juancastro.digitaldiary.tags.Tag;
import dev.juancastro.digitaldiary.tags.TagRepository;
import dev.juancastro.digitaldiary.tags.TagType;

@ExtendWith(MockitoExtension.class)
class DataLoaderTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private DataLoader dataLoader;

    @Test
    void whenAllTagsDoNotExist_thenSaveAll() throws Exception {
        for (TagType type : TagType.values()) {
            when(tagRepository.existsByName(type)).thenReturn(false);
        }

        dataLoader.run();

        int typesCount = TagType.values().length;
        verify(tagRepository, times(typesCount)).save(any(Tag.class));

        for (TagType type : TagType.values()) {
            verify(tagRepository).existsByName(type);
        }
    }

    @Test
    void whenAllTagsExist_thenDoNotSaveAny() throws Exception {
        for (TagType type : TagType.values()) {
            when(tagRepository.existsByName(type)).thenReturn(true);
        }

        dataLoader.run();

        verify(tagRepository, never()).save(any(Tag.class));

        for (TagType type : TagType.values()) {
            verify(tagRepository).existsByName(type);
        }
    }

    @Test
    void whenSomeTagsExist_thenSaveOnlyMissing() throws Exception {
 
        when(tagRepository.existsByName(TagType.PERSONAL)).thenReturn(true);
        for (TagType type : TagType.values()) {
            if (type != TagType.PERSONAL) {
                when(tagRepository.existsByName(type)).thenReturn(false);
            }
        }

        dataLoader.run();

        verify(tagRepository, never()).save(argThat(tag -> tag.getName() == TagType.PERSONAL));
        int toSaveCount = TagType.values().length - 1;
        verify(tagRepository, times(toSaveCount)).save(any(Tag.class));
    }
}