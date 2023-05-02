package de.hilsky.bookservice.service;

import de.hilsky.bookservice.dtos.JokeResponseDTO;
import de.hilsky.bookservice.exception.MissingDataException;
import de.hilsky.bookservice.model.Book;
import de.hilsky.bookservice.openai.ChatGptService;
import de.hilsky.bookservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JokeService {

    private final ChatGptService chatGptService;
    private final BookRepository bookRepository;

    public JokeResponseDTO tellJoke(UUID bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new MissingDataException("Book for joke not found"));

        String prompt = String.format("Tell me a joke from the universe of the book %s", book.getTitle());
        log.info("Prompt for joke is {}", prompt);

        String joke = chatGptService.getChatCompletions(prompt);
        log.info("The funny joke for {} is: {}", book.getTitle(), joke);
        return new JokeResponseDTO(book.getId(), joke);
    }
}
