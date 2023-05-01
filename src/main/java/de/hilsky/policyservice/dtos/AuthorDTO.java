package de.hilsky.policyservice.dtos;

import java.util.UUID;

public record AuthorDTO(
        UUID id,
        String firstName,
        String lastName
) {
}
