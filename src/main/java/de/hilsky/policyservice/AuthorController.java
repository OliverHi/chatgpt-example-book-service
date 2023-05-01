package de.hilsky.policyservice;

import de.hilsky.policyservice.dtos.AuthorCreateDTO;
import de.hilsky.policyservice.dtos.AuthorDTO;
import de.hilsky.policyservice.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping(value = "/authors",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuthorDTO> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping(value = "/author/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthorDTO getAuthor(@PathVariable UUID id) {
        return authorService.getAuthor(id);
    }

    @PostMapping(value = "/authors",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthorDTO createAuthor(@Valid @RequestBody AuthorCreateDTO authorCreateDTO) {
        return authorService.createAuthor(authorCreateDTO);
    }
}
