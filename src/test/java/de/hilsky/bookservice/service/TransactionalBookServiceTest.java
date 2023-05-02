package de.hilsky.bookservice.service;

import de.hilsky.bookservice.model.Author;
import de.hilsky.bookservice.model.Book;
import de.hilsky.bookservice.model.BookWithAuthor;
import de.hilsky.bookservice.repository.AuthorRepository;
import de.hilsky.bookservice.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionalBookServiceTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    TransactionalBookService transactionalBookService;

    @Test
    void createBookOfNewAuthor_shouldCreateBookAndAuthor() {
        // given
        UUID bookId = UUID.randomUUID();
        String isbn = "12345";
        String title = "Book 1";
        String description = "Description 1";
        Book book = new Book(bookId, null, isbn, title, description);

        UUID authorId = UUID.randomUUID();
        String firstName = "John";
        String lastName = "Doe";
        Author author = new Author(authorId, firstName, lastName);

        when(authorRepository.save(any(Author.class))).thenReturn(new Author(authorId, firstName, lastName));
        when(bookRepository.save(any(Book.class))).thenReturn(new Book(bookId, authorId, isbn, title, description));

        // when
        BookWithAuthor result = transactionalBookService.createBookOfNewAuthor(book, author);

        // then
        assertEquals(bookId, result.getBook().getId());
        assertEquals(authorId, result.getAuthor().getId());
        assertEquals(title, result.getBook().getTitle());
        assertEquals(isbn, result.getBook().getIsbn());
        assertEquals(description, result.getBook().getDescription());
        assertEquals(firstName, result.getAuthor().getFirstName());
        assertEquals(lastName, result.getAuthor().getLastName());
    }
}