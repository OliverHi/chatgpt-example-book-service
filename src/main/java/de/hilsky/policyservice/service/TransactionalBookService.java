package de.hilsky.policyservice.service;

import de.hilsky.policyservice.model.Author;
import de.hilsky.policyservice.model.Book;
import de.hilsky.policyservice.model.BookWithAuthor;
import de.hilsky.policyservice.repository.AuthorRepository;
import de.hilsky.policyservice.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TransactionalBookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;


    public BookWithAuthor createBookOfNewAuthor(Book book, Author author) {
        author.setId(UUID.randomUUID());
        Author newAuthor = authorRepository.save(author);
        log.debug("Created new author {}", newAuthor);

        book.setId(UUID.randomUUID());
        book.setAuthor(newAuthor.getId());
        Book newBook = bookRepository.save(book);
        log.debug("Created new book {}", newBook);

        BookWithAuthor bookWithAuthor = new BookWithAuthor(newBook, newAuthor);
        log.info("Saved new book {} with author {} {}", newBook.getTitle(), newAuthor.getFirstName(), newAuthor.getLastName());
        return bookWithAuthor;
    }
}
