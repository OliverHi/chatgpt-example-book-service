package de.hilsky.bookservice.repository;

import de.hilsky.bookservice.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {
    List<Author> findAllByIdIn(List<UUID> ids);
}
