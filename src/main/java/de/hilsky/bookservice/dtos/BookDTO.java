package de.hilsky.bookservice.dtos;

import java.util.UUID;

public record BookDTO (
        UUID id,
        String author,
        String isbn,
        String title,
        String description
){ }
