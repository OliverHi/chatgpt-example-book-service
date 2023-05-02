package de.hilsky.bookservice.service;

import de.hilsky.bookservice.dtos.AuthorCreateDTO;
import de.hilsky.bookservice.dtos.AuthorDTO;
import de.hilsky.bookservice.exception.MissingDataException;
import de.hilsky.bookservice.model.Author;
import de.hilsky.bookservice.repository.AuthorRepository;
import de.hilsky.bookservice.util.DTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    public List<AuthorDTO> getAllAuthors() {
        List<AuthorDTO> authors = authorRepository.findAll().stream()
                .map(DTOMapper::convert)
                .toList();
        log.info("Found {} authors", authors.size());
        log.debug("Returning authors: {}", authors);
        return authors;
    }

    public AuthorDTO getAuthor(UUID id) {
        log.info("Looking for author {}", id);
        return authorRepository.findById(id)
                .map(DTOMapper::convert)
                .orElseThrow(() -> new MissingDataException("Author not found"));
    }

    public AuthorDTO createAuthor(AuthorCreateDTO authorCreateDTO) {
        Author author = Author.builder()
                .id(UUID.randomUUID())
                .firstName(authorCreateDTO.firstName())
                .lastName(authorCreateDTO.lastName())
                .build();
        log.debug("Creating new author {}", author);

        Author newAuthor = authorRepository.save(author);
        log.info("Created new author {}", newAuthor);

        return DTOMapper.convert(newAuthor);
    }
}
