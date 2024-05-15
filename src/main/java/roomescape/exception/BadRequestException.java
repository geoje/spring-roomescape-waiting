package roomescape.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpStatusException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
