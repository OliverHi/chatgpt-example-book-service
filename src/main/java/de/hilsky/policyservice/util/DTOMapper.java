package de.hilsky.policyservice.util;

import de.hilsky.policyservice.dtos.AuthorDTO;
import de.hilsky.policyservice.dtos.BookDTO;
import de.hilsky.policyservice.model.Author;
import de.hilsky.policyservice.model.BookWithAuthor;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class DTOMapper {

    public static List<BookDTO> convert(List<BookWithAuthor> all) {
        return all.stream()
                .map(DTOMapper::convert)
                .toList();
    }

    public static AuthorDTO convert(Author author) {
        return new AuthorDTO(
                author.getId(),
                author.getFirstName(),
                author.getLastName()
        );
    }

    public static BookDTO convert(BookWithAuthor bookOfAuthor) {
        return new BookDTO(
                bookOfAuthor.getBook().getId(),
                bookOfAuthor.getFullAuthorName(),
                bookOfAuthor.getBook().getIsbn(),
                bookOfAuthor.getBook().getIsbn(),
                bookOfAuthor.getBook().getDescription()
        );
    }
}
