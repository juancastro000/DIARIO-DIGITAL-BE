package dev.juancastro.digitaldiary.tags;

import jakarta.persistence.*;

@Entity
@Table(name = "tags")
public class Tag {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false, length = 20)
    private TagType name;

    public Tag(TagType name) {
        this.name = name;
    }

    public Tag() {
    }


    public Tag(long l, TagType personal) {
    }

    public Long getId() {
        return id;
    }

    public TagType getName() {
        return name;
    }

    public void setName(TagType name) {
        this.name = name;
    }
    
}