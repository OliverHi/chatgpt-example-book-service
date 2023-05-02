package de.hilsky.bookservice.openai;

import de.hilsky.bookservice.openai.dtos.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGptService {
    private final ChatGptApi chatGptApi;
    private final OpenAiConfig config;

    public String getChatCompletions(String prompt) {
        OpenAiRequest request = new OpenAiRequest();
        request.setModel(config.getModel());

        OpenAIMessage message = new OpenAIMessage(OpenAiRoles.USER.getValue(), prompt);
        request.setMessages(Collections.singletonList(message));
        log.info("Send request to chatGPT model {}", config.getModel());

        try {
            OpenAiResponse chatCompletions = chatGptApi.getChatCompletions(request, "Bearer " + config.getKey());
            String answer = Optional.ofNullable(chatCompletions)
                    .map(OpenAiResponse::getChoices)
                    .filter(openAIChoices -> !openAIChoices.isEmpty())
                    .map(openAIChoices -> openAIChoices.get(0))
                    .map(OpenAIChoice::getMessage)
                    .map(OpenAIMessage::getContent)
                    .orElse("");
            log.info("chatGPT answer: {}", answer);
            return answer;
        } catch (RuntimeException exception) {
            log.error("Error during call to ChatGpt", exception);
            return "";
        }
    }
}
