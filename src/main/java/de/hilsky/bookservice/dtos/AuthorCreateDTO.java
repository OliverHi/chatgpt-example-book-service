package de.hilsky.bookservice.dtos;

import jakarta.validation.constraints.NotEmpty;

public record AuthorCreateDTO(
        @NotEmpty
        String firstName,
        @NotEmpty
        String lastName
) {
}
