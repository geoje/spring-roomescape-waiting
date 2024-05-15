package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends HttpStatusException {

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
