package de.hilsky.bookservice.openai.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAIMessage {
    private String role;
    private String content;
}
