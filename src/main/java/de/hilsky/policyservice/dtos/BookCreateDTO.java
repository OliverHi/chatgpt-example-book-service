package de.hilsky.policyservice.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

public record BookCreateDTO(
        @NotEmpty
        String title,
        @Nullable
        String isbn,
        @Nullable
        UUID authorId,
        @Nullable
        AuthorCreateDTO authorCreateDTO
) {
}
