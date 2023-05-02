package de.hilsky.bookservice.openai.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAIUsage {
    private Integer prompt_tokens;
    private Integer completion_tokens;
    private Integer total_tokens;
}
