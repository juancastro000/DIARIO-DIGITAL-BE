package dev.juancastro.digitaldiary.entries;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import dev.juancastro.digitaldiary.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "entries")
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entry")
    private Long id;

    @NotBlank(message = "Content cannot be empty")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @NotNull
    @Column(nullable = false, updatable = false)
    private LocalDateTime date;

    @NotBlank(message = "mood cannot be empty")
    @Column(nullable = false)
    private String mood;

    @NotBlank(message = "productivity cannot be empty")
    @Column(nullable = false)
    private String productivity;

    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "entry_tags",
        joinColumns = @JoinColumn(name = "entry_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    public Entry() {
    }

    public Entry(String content, LocalDateTime date, String mood, String productivity) {
        this.content = content;
        this.date = date;
        this.mood = mood;
        this.productivity = productivity;
    }

    @PrePersist
    protected void onCreate() {
        if (date == null) {
            date = LocalDateTime.now();
        }
    }

    private void setDate(LocalDateTime date) {
        this.date = date;
    }

    
}
