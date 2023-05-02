package de.hilsky.bookservice.dtos;

import java.util.UUID;

public record JokeResponseDTO(
        UUID bookId,
        String joke
) {
}
