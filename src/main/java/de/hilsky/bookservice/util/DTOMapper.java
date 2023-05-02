package de.hilsky.bookservice.util;

import de.hilsky.bookservice.dtos.AuthorDTO;
import de.hilsky.bookservice.dtos.BookDTO;
import de.hilsky.bookservice.model.Author;
import de.hilsky.bookservice.model.BookWithAuthor;
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
                bookOfAuthor.getBook().getTitle(),
                bookOfAuthor.getBook().getDescription()
        );
    }
}
