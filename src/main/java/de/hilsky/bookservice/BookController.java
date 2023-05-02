package de.hilsky.bookservice;

import de.hilsky.bookservice.dtos.BookCreateDTO;
import de.hilsky.bookservice.dtos.BookDTO;
import de.hilsky.bookservice.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping(value = "/books",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping(value = "/books/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BookDTO getBook(@PathVariable UUID id) {
        return bookService.getBook(id);
    }

    @PostMapping(value = "/books",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BookDTO createBook(@Valid @RequestBody BookCreateDTO bookCreateDTO) {
        return bookService.createBook(bookCreateDTO);
    }
}
