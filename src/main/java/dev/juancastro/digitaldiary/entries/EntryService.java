package dev.juancastro.digitaldiary.entries;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.juancastro.digitaldiary.entries.exceptions.ResourceNotFoundException;
import dev.juancastro.digitaldiary.entries.exceptions.AccessDeniedException;
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

        User user = getAuthenticatedUser();

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
        User user = getAuthenticatedUser();
        Entry entry = entryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entry not found with id: " + id));
        
        if (!entry.getUser().equals(user)) {
            throw new AccessDeniedException("You are not authorized to access this entry");
        }
        
        return EntryMapper.toDto(entry);
    }

    public List<EntryDto> getAllEntries() {
        User user = getAuthenticatedUser();
        return entryRepository.findByUser(user)
                .stream()
                .map(EntryMapper::toDto)
                .collect(Collectors.toList());
    }


    public EntryDto updateEntry(Long id, EntryDto dto) {

        User user = getAuthenticatedUser();
        Entry existingEntry = entryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entry not found with id: " + id));
        
        if (!existingEntry.getUser().equals(user)) {
            throw new AccessDeniedException("You are not authorized to update this entry");
        }
        
        existingEntry.setContent(dto.content());
        existingEntry.setMood(dto.mood());
        existingEntry.setProductivity(dto.productivity());
        
        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(dto.tagIds()));
        existingEntry.setTags(tags);
        
        Entry updatedEntry = entryRepository.save(existingEntry);
        return EntryMapper.toDto(updatedEntry);
    }

    public void deleteEntry(Long id) {
        User user = getAuthenticatedUser();
        Entry entry = entryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entry not found with id: " + id));
        
        if (!entry.getUser().equals(user)) {
            throw new AccessDeniedException("You are not authorized to delete this entry");
        }
        entryRepository.deleteById(id);
    }
}
