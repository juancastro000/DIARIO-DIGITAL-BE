package dev.juancastro.digitaldiary.entries;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.juancastro.digitaldiary.users.User;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long>  {
    List<Entry> findByUser(User user);
}
