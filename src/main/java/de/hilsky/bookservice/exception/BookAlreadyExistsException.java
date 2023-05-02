package de.hilsky.bookservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException() {
        super("The book already exists");
    }

    public BookAlreadyExistsException(String message) {
        super(message);
    }
}
