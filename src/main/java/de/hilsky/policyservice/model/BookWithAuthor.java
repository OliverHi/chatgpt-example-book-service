package de.hilsky.policyservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookWithAuthor {

    private static final String AUTHOR = "%s %s";

    private Book book;
    private Author author;

    public String getFullAuthorName() {
        return AUTHOR.formatted(author.getFirstName(), author.getLastName());
    }
}
