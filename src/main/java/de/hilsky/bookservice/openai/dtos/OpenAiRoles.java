package de.hilsky.bookservice.openai.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OpenAiRoles {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant");

    private final String value;
}
