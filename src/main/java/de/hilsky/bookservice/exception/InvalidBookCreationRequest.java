package de.hilsky.bookservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidBookCreationRequest extends RuntimeException {
    public InvalidBookCreationRequest() {
        super("The book creation request is not valid");
    }

    public InvalidBookCreationRequest(String message) {
        super(message);
    }
}
