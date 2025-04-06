package dev.juancastro.digitaldiary.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import dev.juancastro.digitaldiary.tags.Tag;
import dev.juancastro.digitaldiary.tags.TagRepository;
import dev.juancastro.digitaldiary.tags.TagType;

@Component
public class DataLoader implements CommandLineRunner {

    private final TagRepository tagRepository;

    public DataLoader(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public void run(String... args) {
        for (TagType type : TagType.values()) {
            CreateTagIfNonExists(type);
        }
    }

    private void CreateTagIfNonExists(TagType name) {
        if (!tagRepository.existsByName(name)) {
            Tag tag = new Tag(name);
            tagRepository.save(tag);
        }
    }
}