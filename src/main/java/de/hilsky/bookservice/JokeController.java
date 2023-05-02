package de.hilsky.bookservice;

import de.hilsky.bookservice.dtos.JokeRequestDTO;
import de.hilsky.bookservice.dtos.JokeResponseDTO;
import de.hilsky.bookservice.service.JokeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JokeController {

    private final JokeService jokeService;
    @PostMapping(value = "/jokes",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public JokeResponseDTO tellJoke(@Valid @RequestBody JokeRequestDTO jokeRequestDTO) {
        return jokeService.tellJoke(jokeRequestDTO.bookId());
    }

}
