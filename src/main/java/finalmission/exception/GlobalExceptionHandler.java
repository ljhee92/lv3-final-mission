package finalmission.exception;

import finalmission.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "[ERROR] 알 수 없는 오류가 발생했습니다.";

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = e.getMessage();
        log.warn(message, e);
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(status, message));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String message = e.getMessage();
        log.warn(message, e);
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(status, message));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException e) {
        HttpStatus status = e.getStatus();
        String message = e.getMessage();
        log.warn(message, e);
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(status, message));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNaverApiException(ApiException e) {
        HttpStatusCode statusCode = e.getStatusCode();
        HttpStatus status = HttpStatus.valueOf(statusCode.value());
        String message = e.getMessage();
        log.warn(message, e);
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(status, message));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleInternalServerException(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error(INTERNAL_SERVER_ERROR_MESSAGE, e);
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(status, INTERNAL_SERVER_ERROR_MESSAGE));
    }
}
