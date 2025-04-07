package dev.juancastro.digitaldiary.entries;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.juancastro.digitaldiary.tags.Tag;
import dev.juancastro.digitaldiary.tags.TagRepository;
import dev.juancastro.digitaldiary.users.User;
import dev.juancastro.digitaldiary.users.UserRepository;

@Service
public class EntryService {
    private final EntryRepository entryRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public EntryService(EntryRepository entryRepository, TagRepository tagRepository, UserRepository userRepository){
        this.entryRepository = entryRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username;
    if (principal instanceof UserDetails userDetails) {
        username = userDetails.getUsername();
    } else {
        username = principal.toString();
    }

    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
}

    public EntryDto createEntry(EntryDto dto, String username){

        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(dto.tagIds()));

        Entry entry = new Entry(
                dto.content(),
                dto.date(),
                dto.mood(),
                dto.productivity(),
                user 
        );

        entry.setTags(tags);

        return EntryMapper.toDto(entryRepository.save(entry));
    }

    public EntryDto getEntryById(Long id) {
        Entry entry = entryRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Entry not found with id" + id));
        return EntryMapper.toDto(entry);
    }

     public List<EntryDto> getAllEntries() {
        return entryRepository.findAll()
                .stream()
                .map(EntryMapper::toDto)
                .collect(Collectors.toList());
    }

    public EntryDto updateEntry(Long id, EntryDto dto) {
        Entry existingEntry = entryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found with id: " + id));
        
        existingEntry.setContent(dto.content());
        existingEntry.setMood(dto.mood());
        existingEntry.setProductivity(dto.productivity());
        
        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(dto.tagIds()));
        existingEntry.setTags(tags);
        
        Entry updatedEntry = entryRepository.save(existingEntry);
        return EntryMapper.toDto(updatedEntry);
    }

    public void deleteEntry(Long id) {
        if (!entryRepository.existsById(id)) {
            throw new RuntimeException("Entry not found with id: " + id);
        }
        entryRepository.deleteById(id);
    }
}
