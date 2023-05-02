package de.hilsky.bookservice.openai.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiResponse {
    private String id;
    private String object;
    private Long created;
    private String model;
    private OpenAIUsage usage;
    private List<OpenAIChoice> choices;
}
