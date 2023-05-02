package de.hilsky.bookservice;

import de.hilsky.bookservice.openai.ChatGptApi;
import de.hilsky.bookservice.openai.dtos.*;
import lombok.experimental.UtilityClass;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@UtilityClass
public class TestUtil {

    public static OpenAiResponse getOpenAiTestResponse(String description) {
        OpenAiResponse expectedResponse = new OpenAiResponse();
        OpenAIChoice openAiChoice = new OpenAIChoice(new OpenAIMessage(OpenAiRoles.ASSISTANT.getValue(), description), null, null);
        expectedResponse.setChoices(Collections.singletonList(openAiChoice));
        return expectedResponse;
    }

    public static OpenAiResponse getChatCompletions(ChatGptApi mockChatGptApi) {
        return mockChatGptApi.getChatCompletions(any(OpenAiRequest.class), anyString());
    }
}
