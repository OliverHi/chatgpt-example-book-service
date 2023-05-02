package de.hilsky.bookservice.dtos;

import java.util.UUID;

public record JokeRequestDTO(
        UUID bookId
) {
}
