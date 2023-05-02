package de.hilsky.bookservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingDataException extends RuntimeException {
    public MissingDataException() {
        super("Some data is missing");
    }

    public MissingDataException(String message) {
        super(message);
    }
}
