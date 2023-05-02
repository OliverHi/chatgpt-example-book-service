package de.hilsky.bookservice.service;

import de.hilsky.bookservice.dtos.AuthorCreateDTO;
import de.hilsky.bookservice.dtos.BookCreateDTO;
import de.hilsky.bookservice.dtos.BookDTO;
import de.hilsky.bookservice.exception.BookAlreadyExistsException;
import de.hilsky.bookservice.exception.InvalidBookCreationRequest;
import de.hilsky.bookservice.exception.MissingDataException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private TransactionalBookService transactionalBookService;

    @InjectMocks
    private BookService bookService;

    @Test
    void getAllBooks_shouldReturnAllBooks() {
        // given
        List<Book> books = new ArrayList<>();
        books.add(new Book(UUID.randomUUID(), UUID.randomUUID(), "12345", "Book 1", "Description 1"));
        books.add(new Book(UUID.randomUUID(), UUID.randomUUID(), "67890", "Book 2", "Description 2"));
        when(bookRepository.findAll()).thenReturn(books);

        List<Author> authors = new ArrayList<>();
        authors.add(new Author(books.get(0).getAuthor(), "John", "Doe"));
        authors.add(new Author(books.get(1).getAuthor(), "Jane", "Doe"));
        when(authorRepository.findAllByIdIn(anyList())).thenReturn(authors);

        // when
        List<BookDTO> bookDTOs = bookService.getAllBooks();

        // then
        assertEquals(2, bookDTOs.size());

        assertEquals(books.get(0).getId(), bookDTOs.get(0).id());
        assertEquals(books.get(0).getTitle(), bookDTOs.get(0).title());
        assertEquals("John Doe", bookDTOs.get(0).author());
        assertEquals(books.get(0).getIsbn(), bookDTOs.get(0).isbn());
        assertEquals(books.get(0).getDescription(), bookDTOs.get(0).description());

        assertEquals(books.get(1).getId(), bookDTOs.get(1).id());
        assertEquals(books.get(1).getTitle(), bookDTOs.get(1).title());
        assertEquals("Jane Doe", bookDTOs.get(1).author());
        assertEquals(books.get(1).getIsbn(), bookDTOs.get(1).isbn());
        assertEquals(books.get(1).getDescription(), bookDTOs.get(1).description());
    }

    @Test
    void getBook_shouldReturnBookById() {
        // given
        UUID bookId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        String isbn = "12345";
        String title = "Book 1";
        String description = "Description 1";

        Book book = new Book(bookId, authorId, isbn, title, description);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Author author = new Author(authorId, "John", "Doe");
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        // when
        BookDTO bookDTO = bookService.getBook(bookId);

        // then
        assertNotNull(bookDTO);
        assertNotNull(bookDTO.id());
        assertEquals(title, bookDTO.title());
        assertEquals("John Doe", bookDTO.author());
        assertEquals(isbn, bookDTO.isbn());
        assertEquals(description, bookDTO.description());
    }

    @Test
    void getBook_notFound() {
        // given
        UUID bookId = UUID.randomUUID();
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MissingDataException.class, () -> bookService.getBook(bookId));
    }

    @Test
    void getBook_authorNotFound() {
        // given
        UUID bookId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        String isbn = "12345";
        String title = "Book 1";
        String description = "Description 1";

        Book book = new Book(bookId, authorId, isbn, title, description);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MissingDataException.class, () -> bookService.getBook(bookId));
    }

    @Test
    void createBook_requestMissingAuthorInfo() {
        // given
        String isbn = "12345";
        String title = "Book 1";
        BookCreateDTO bookCreateDTO = new BookCreateDTO(title, isbn, null, null);

        // when & then
        assertThrows(InvalidBookCreationRequest.class, () -> bookService.createBook(bookCreateDTO));
    }

    @Test
    void createBook_bookAlreadyExists() {
        // given
        UUID bookId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        String isbn = "12345";
        String title = "Book 1";
        String description = "Description 1";
        Book book = new Book(bookId, authorId, isbn, title, description);
        when(bookRepository.findByTitleOrIsbn(title, isbn)).thenReturn(Optional.of(book));

        BookCreateDTO bookCreateDTO = new BookCreateDTO(title, isbn, authorId, null);

        // when & then
        assertThrows(BookAlreadyExistsException.class, () -> bookService.createBook(bookCreateDTO));
    }

    @Test
    void createBook_shouldCreateBookForExistingAuthor() {
        // given
        UUID authorId = UUID.randomUUID();
        String isbn = "12345";
        String title = "Book 1";
        BookCreateDTO bookCreateDTO = new BookCreateDTO(title, isbn, authorId, null);

        Author author = new Author(authorId, "John", "Doe");
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        UUID bookId = UUID.randomUUID();
        Book newBook = new Book(bookId, authorId, isbn, title, "");
        when(bookRepository.save(any(Book.class))).thenReturn(newBook);

        // when
        BookDTO bookDTO = bookService.createBook(bookCreateDTO);

        // then
        assertNotNull(bookDTO);
        assertNotNull(bookDTO.id());
        assertEquals(title, bookDTO.title());
        assertEquals("John Doe", bookDTO.author());
        assertEquals(isbn, bookDTO.isbn());
        assertEquals("", bookDTO.description());
    }

    @Test
    void createBook_authorDoesNotExist() {
        // given
        UUID authorId = UUID.randomUUID();
        String isbn = "12345";
        String title = "Book 1";
        BookCreateDTO bookCreateDTO = new BookCreateDTO(title, isbn, authorId, null);

        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MissingDataException.class, () -> bookService.createBook(bookCreateDTO));
    }

    @Test
    void createBook_shouldCreateBookAndNewAuthor() {
        // given
        UUID bookId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        String isbn = "12345";
        String title = "Book 1";

        Author author = new Author(authorId, "John", "Doe");
        Book newBook = new Book(bookId, authorId, isbn, title, "");

        BookWithAuthor bookWithAuthor = new BookWithAuthor(newBook, author);
        when(transactionalBookService.createBookOfNewAuthor(any(), any())).thenReturn(bookWithAuthor);

        AuthorCreateDTO authorCreateDTO = new AuthorCreateDTO("John", "Doe");
        BookCreateDTO bookCreateDTO = new BookCreateDTO(title, isbn, null, authorCreateDTO);

        // when
        BookDTO bookDTO = bookService.createBook(bookCreateDTO);

        // then
        assertNotNull(bookDTO);
        assertNotNull(bookDTO.id());
        assertEquals(title, bookDTO.title());
        assertEquals("John Doe", bookDTO.author());
        assertEquals(isbn, bookDTO.isbn());
        assertEquals("", bookDTO.description());
    }
}