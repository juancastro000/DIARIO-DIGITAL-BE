package dev.juancastro.digitaldiary.entries;

import java.time.LocalDateTime;
import java.util.Set;


public record EntryDto(
    Long id,
    String content,
    LocalDateTime date,
    String mood,
    String productivity,
    Set<Long> tagIds,
    Long userId
) {}