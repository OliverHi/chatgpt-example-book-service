package de.hilsky.bookservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(value = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(value = "/testBooks.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAllBooks_shouldReturnListOfBooks() throws Exception {

        mockMvc.perform(get("/books")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value("00000000-0000-0000-0000-000000000005"))
                .andExpect(jsonPath("[0].author").value("John Ronald Reuel Tolkien"))
                .andExpect(jsonPath("[0].isbn").value("1234567890"))
                .andExpect(jsonPath("[0].title").value("Lord of the Rings"))
                .andExpect(jsonPath("[0].description").value("Description 1"))

                .andExpect(jsonPath("[1].id").value("00000000-0000-0000-0000-000000000006"))
                .andExpect(jsonPath("[1].author").value("Joanne K. Rowling"))
                .andExpect(jsonPath("[1].isbn").value("0987654321"))
                .andExpect(jsonPath("[1].title").value("Harry Potter and the Philosophers Stone"))
                .andExpect(jsonPath("[1].description").value("Description 2"))

                .andExpect(jsonPath("[2].id").value("00000000-0000-0000-0000-000000000007"))
                .andExpect(jsonPath("[2].author").value("William Shakespeare"))
                .andExpect(jsonPath("[2].isbn").value("2468101214"))
                .andExpect(jsonPath("[2].title").value("Hamlet"))
                .andExpect(jsonPath("[2].description").value("Description 3"))

                .andExpect(jsonPath("[3].id").value("00000000-0000-0000-0000-000000000008"))
                .andExpect(jsonPath("[3].author").value("Stephen King"))
                .andExpect(jsonPath("[3].isbn").value("1357924680"))
                .andExpect(jsonPath("[3].title").value("It"))
                .andExpect(jsonPath("[3].description").value("Description 4"));
    }

    @Test
    @Sql(value = "/testBooks.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getBook_shouldReturnBook() throws Exception {
        mockMvc.perform(get("/books/00000000-0000-0000-0000-000000000005")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value("00000000-0000-0000-0000-000000000005"))
                .andExpect(jsonPath("author").value("John Ronald Reuel Tolkien"))
                .andExpect(jsonPath("isbn").value("1234567890"))
                .andExpect(jsonPath("title").value("Lord of the Rings"))
                .andExpect(jsonPath("description").value("Description 1"));
    }

    @Test
    @Sql(value = "/testBooks.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createBook_shouldCreateBookForExistingAuthor() throws Exception {
        String requestBody = """
                {
                  "title": "Under the dome",
                  "isbn": "42424242",
                  "authorId": "00000000-0000-0000-0000-000000000004"
                }
                """;

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("author").value("Stephen King"))
                .andExpect(jsonPath("isbn").value("42424242"))
                .andExpect(jsonPath("title").value("Under the dome"))
                .andExpect(jsonPath("description").value(""));
    }

    @Test
    @Sql(value = "/testBooks.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createBook_shouldCreateBookAndAuthor() throws Exception {
        String requestBody = """
                {
                  "title": "Example Book",
                  "isbn": "424242424242",
                  "authorCreateDTO": {
                    "firstName": "John",
                    "lastName": "Doe"
                  }
                }
                """;

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("author").value("John Doe"))
                .andExpect(jsonPath("isbn").value("424242424242"))
                .andExpect(jsonPath("title").value("Example Book"))
                .andExpect(jsonPath("description").value(""));
    }
}