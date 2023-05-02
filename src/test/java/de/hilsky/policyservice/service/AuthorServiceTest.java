package de.hilsky.policyservice.service;

import de.hilsky.policyservice.dtos.AuthorCreateDTO;
import de.hilsky.policyservice.dtos.AuthorDTO;
import de.hilsky.policyservice.exception.MissingDataException;
import de.hilsky.policyservice.model.Author;
import de.hilsky.policyservice.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    AuthorService authorService;

    @Test
    void getAllAuthors_shouldReturnAllAuthors() {
        // Given
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(UUID.randomUUID(), "John", "Doe"));
        authors.add(new Author(UUID.randomUUID(), "Jane", "Doe"));
        when(authorRepository.findAll()).thenReturn(authors);

        // When
        List<AuthorDTO> authorDTOs = authorService.getAllAuthors();

        // Then
        assertEquals(2, authorDTOs.size());
        assertEquals(authors.get(0).getId(), authorDTOs.get(0).id());
        assertEquals(authors.get(0).getFirstName(), authorDTOs.get(0).firstName());
        assertEquals(authors.get(0).getLastName(), authorDTOs.get(0).lastName());
        assertEquals(authors.get(1).getId(), authorDTOs.get(1).id());
        assertEquals(authors.get(1).getFirstName(), authorDTOs.get(1).firstName());
        assertEquals(authors.get(1).getLastName(), authorDTOs.get(1).lastName());
    }

    @Test
    void getAuthor_shouldReturnAuthorById() {
        // Given
        UUID authorId = UUID.randomUUID();
        String firstName = "John";
        String lastName = "Doe";
        Author author = new Author(authorId, firstName, lastName);
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        // When
        AuthorDTO authorDTO = authorService.getAuthor(authorId);

        // Then
        assertEquals(author.getId(), authorDTO.id());
        assertEquals(author.getFirstName(), authorDTO.firstName());
        assertEquals(author.getLastName(), authorDTO.lastName());
    }

    @Test
    void getAuthor_notFound() {
        // Given
        UUID authorId = UUID.randomUUID();
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        // When & then
        assertThrows(MissingDataException.class, () -> authorService.getAuthor(authorId));
    }

    @Test
    void createAuthor_shouldCreateNewAuthor() {
        // Given
        UUID authorId = UUID.randomUUID();
        String firstName = "John";
        String lastName = "Doe";
        Author author = new Author(authorId, firstName, lastName);
        when(authorRepository.save(any(Author.class))).thenReturn(author);


        // When
        AuthorCreateDTO authorCreateDTO = new AuthorCreateDTO(firstName, lastName);
        AuthorDTO authorDTO = authorService.createAuthor(authorCreateDTO);

        // Then
        assertEquals(authorId, authorDTO.id());
        assertEquals(firstName, authorDTO.firstName());
        assertEquals(lastName, authorDTO.lastName());
    }
}