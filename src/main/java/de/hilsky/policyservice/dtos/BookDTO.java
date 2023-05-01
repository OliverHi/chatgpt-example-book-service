package de.hilsky.policyservice.dtos;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

public record BookDTO (
        UUID id,
        String author,
        String isbn,
        String title,
        String description
){ }
