package de.hilsky.bookservice.openai.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAIChoice {
    private OpenAIMessage message;
    private String finish_reason;
    private Integer index;
}
