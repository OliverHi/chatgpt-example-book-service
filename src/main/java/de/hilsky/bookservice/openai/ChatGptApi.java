package de.hilsky.bookservice.openai;

import de.hilsky.bookservice.openai.dtos.OpenAiRequest;
import de.hilsky.bookservice.openai.dtos.OpenAiResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "openai", url = "${openai.api.url}")
public interface ChatGptApi {
    @RequestMapping(method = RequestMethod.POST,
            value = "/completions",
            consumes = "application/json",
            produces = "application/json")
    OpenAiResponse getChatCompletions(@Valid @RequestBody OpenAiRequest request, @RequestHeader("Authorization") String accessToken);
}
