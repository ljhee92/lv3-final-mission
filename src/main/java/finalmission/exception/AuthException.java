package finalmission.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthException extends RuntimeException {

    private HttpStatus status = HttpStatus.UNAUTHORIZED;

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
