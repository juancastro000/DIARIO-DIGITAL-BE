package dev.juancastro.digitaldiary.entries;
import dev.juancastro.digitaldiary.tags.Tag;
import dev.juancastro.digitaldiary.users.User;

import java.util.Set;
import java.util.stream.Collectors;

public class EntryMapper {

      public static EntryDto toDto(Entry entry) {
        Set<Long> tagIds = entry.getTags()
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());

        return new EntryDto(
                entry.getId(),
                entry.getContent(),
                entry.getDate(),
                entry.getMood(),
                entry.getProductivity(),
                tagIds,
                entry.getUser().getId()
        );
    }
    
    public static Entry toEntity(EntryDto dto, Set<Tag> tags, User user) {
        Entry entry = new Entry(
                dto.content(),
                dto.date(),
                dto.mood(),
                dto.productivity(),
                user
        );
        entry.setTags(tags);
        return entry;
    }
}
