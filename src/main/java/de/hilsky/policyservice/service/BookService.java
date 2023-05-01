package de.hilsky.policyservice.service;

import de.hilsky.policyservice.dtos.BookCreateDTO;
import de.hilsky.policyservice.dtos.BookDTO;
import de.hilsky.policyservice.exception.BookAlreadyExistsException;
import de.hilsky.policyservice.exception.InvalidBookCreationRequest;
import de.hilsky.policyservice.exception.MissingDataException;
import de.hilsky.policyservice.model.Author;
import de.hilsky.policyservice.model.Book;
import de.hilsky.policyservice.model.BookWithAuthor;
import de.hilsky.policyservice.repository.AuthorRepository;
import de.hilsky.policyservice.repository.BookRepository;
import de.hilsky.policyservice.util.DTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final TransactionalBookService transactionalBookService;

    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<UUID> authorIds = books.stream()
                .map(Book::getAuthor)
                .collect(Collectors.toList());
        List<Author> authors = authorRepository.findAllByIdIn(authorIds);
        log.info("Found {} books in the db", books.size());

        List<BookWithAuthor> bookWithAuthors = enrich(books, authors);
        log.debug("Returning all books: {}", bookWithAuthors);

        return DTOMapper.convert(bookWithAuthors);
    }

    private List<BookWithAuthor> enrich(List<Book> books, List<Author> authors) {
        return books.stream()
                .map(book -> {
                    Author matchingAuthor = authors.stream()
                            .filter(author -> author.getId() == book.getId())
                            .findFirst()
                            .orElse(null);
                    log.warn("Found book {} without valid author (id={})", book.getId(), book.getId());

                    return new BookWithAuthor(book, matchingAuthor);
                }).toList();
    }

    public BookDTO getBook(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new MissingDataException("Book not found"));
        Author author = authorRepository.findById(book.getAuthor())
                .orElseThrow(() -> new MissingDataException("Author of book not found"));
        log.info("Found book {} with author {}", book, author);

        return DTOMapper.convert(new BookWithAuthor(book, author));
    }

    public BookDTO createBook(BookCreateDTO bookCreateDTO) {
        if (bookCreateDTO.authorId() == null && bookCreateDTO.authorCreateDTO() == null) {
            throw new InvalidBookCreationRequest("You have to provide an author id or create a new author for this book");
        }

        if (bookCreateDTO.authorId() != null) {
            return tryToCeateBookForExistingAuthor(bookCreateDTO);
        } else {
            Book book = new Book(null, null, bookCreateDTO.isbn(), bookCreateDTO.title(), ""); // TODO get description
            Author author = new Author(null, bookCreateDTO.authorCreateDTO().firstName(), bookCreateDTO.authorCreateDTO().lastName());
            log.info("Creating book {}/{} for new author {} {}", book.getTitle(), book.getIsbn(), author.getFirstName(), author.getLastName());
            return DTOMapper.convert(transactionalBookService.createBookOfNewAuthor(book, author));
        }
    }

    private BookDTO tryToCeateBookForExistingAuthor(BookCreateDTO bookCreateDTO) {
        Author author = authorRepository.findById(bookCreateDTO.authorId())
                .orElseThrow(() -> new MissingDataException("This author id does not exist"));

        bookRepository.findByTitleOrIsbn(bookCreateDTO.title(), bookCreateDTO.isbn())
                .ifPresent(book -> {
                    throw new BookAlreadyExistsException("The book already exists with id " + book.getId());
                });

        Book newBook = Book.builder()
                .id(UUID.randomUUID())
                .author(author.getId())
                .isbn(bookCreateDTO.isbn())
                .title(bookCreateDTO.title())
                .description("") // TODO get description from API
                .build();
        log.debug("Creating book {}", newBook);

        Book createdBook = bookRepository.save(newBook);
        log.info("Saved new book {}", createdBook);

        return DTOMapper.convert(createdBook, author)
    }
}
