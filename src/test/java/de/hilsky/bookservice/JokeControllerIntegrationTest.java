package de.hilsky.bookservice;

import de.hilsky.bookservice.openai.ChatGptApi;
import de.hilsky.bookservice.openai.dtos.OpenAiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;

import static de.hilsky.bookservice.TestUtil.getChatCompletions;
import static de.hilsky.bookservice.TestUtil.getOpenAiTestResponse;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(value = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class JokeControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatGptApi mockChatGptApi;

    @Test
    @Sql(value = "/testBooks.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void tellJoke_forExistingBook() throws Exception {
        // Set up the mock response
        String joke = "super funny joke";
        OpenAiResponse expectedResponse = getOpenAiTestResponse(joke);
        when(getChatCompletions(mockChatGptApi)).thenReturn(expectedResponse);

        String requestBody = """
                {
                  "bookId": "00000000-0000-0000-0000-000000000006"
                }
                """;

        mockMvc.perform(post("/jokes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("bookId").value("00000000-0000-0000-0000-000000000006"))
                .andExpect(jsonPath("joke").value(joke));
    }
}