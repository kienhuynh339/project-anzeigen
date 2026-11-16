/**
 * @author Trung Kien Huynh
 */
package sp.backend.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sp.backend.dto.ErrorResponse;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(
            EntityNotFoundException exception) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        return createResponseEntity(exception, status);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleException(
            AccessDeniedException exception) {

        HttpStatus status = HttpStatus.FORBIDDEN;

        return createResponseEntity(exception, status);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleException(
            AuthenticationException exception) {

        HttpStatus status = HttpStatus.UNAUTHORIZED;

        return createResponseEntity(exception, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(
            MethodArgumentNotValidException exception) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return createResponseEntity(exception, status);
    }

    @ExceptionHandler(DeletedItemModificationNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleException(
            DeletedItemModificationNotAllowedException exception) {

        // Gelöschtes Item gilt als nicht existent
        HttpStatus status = HttpStatus.NOT_FOUND;

        return createResponseEntity(exception, status);
    }

    @ExceptionHandler(CannotFavorOwnItemException.class)
    public ResponseEntity<ErrorResponse> handleException(
            CannotFavorOwnItemException exception) {

        HttpStatus status = HttpStatus.FORBIDDEN;

        return createResponseEntity(exception, status);
    }

    @ExceptionHandler(CannotFavorDeletedItemException.class)
    public ResponseEntity<ErrorResponse> handleException(
            CannotFavorDeletedItemException exception) {

        HttpStatus status = HttpStatus.FORBIDDEN;

        return createResponseEntity(exception, status);
    }

    @ExceptionHandler(CannotStartNewChatForDeletedItemException.class)
    public ResponseEntity<ErrorResponse> handleException(
            CannotStartNewChatForDeletedItemException exception) {

        HttpStatus status = HttpStatus.FORBIDDEN;

        return createResponseEntity(exception, status);
    }

    @ExceptionHandler(FavoriteItemAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleException(
            FavoriteItemAlreadyExistsException exception) {

        HttpStatus status = HttpStatus.CONFLICT;

        return createResponseEntity(exception, status);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleException(
            IllegalArgumentException exception) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return createResponseEntity(exception, status);
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(Exception exception, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(exception.getMessage()));
    }
}