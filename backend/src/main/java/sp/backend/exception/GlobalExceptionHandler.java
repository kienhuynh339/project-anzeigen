/**
 * @author Trung Kien Huynh
 */
package sp.backend.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sp.backend.dto.ErrorResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(
            EntityNotFoundException exception) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        return createResponseEntity(exception.getMessage(), status);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleException(
            AccessDeniedException exception) {

        HttpStatus status = HttpStatus.FORBIDDEN;

        return createResponseEntity(exception.getMessage(), status);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleException(
            AuthenticationException exception) {

        HttpStatus status = HttpStatus.UNAUTHORIZED;

        return createResponseEntity(exception.getMessage(), status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception) {

        return handleBindingErrors(exception);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(
            BindException exception) {

        return handleBindingErrors(exception);
    }

    private ResponseEntity<ErrorResponse> handleBindingErrors(
            BindException exception) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    if ("typeMismatch".equals(error.getCode())) {
                        return ExceptionMessage.invalidInput(error.getField());
                    }

                    return error.getDefaultMessage();
                })
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.joining(", "));

        return createResponseEntity(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeletedItemModificationNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleException(
            DeletedItemModificationNotAllowedException exception) {

        // Gelöschtes Item gilt als nicht existent
        HttpStatus status = HttpStatus.NOT_FOUND;

        return createResponseEntity(exception.getMessage(), status);
    }

    @ExceptionHandler(CannotFavorOwnItemException.class)
    public ResponseEntity<ErrorResponse> handleException(
            CannotFavorOwnItemException exception) {

        HttpStatus status = HttpStatus.FORBIDDEN;

        return createResponseEntity(exception.getMessage(), status);
    }

    @ExceptionHandler(CannotFavorDeletedItemException.class)
    public ResponseEntity<ErrorResponse> handleException(
            CannotFavorDeletedItemException exception) {

        HttpStatus status = HttpStatus.FORBIDDEN;

        return createResponseEntity(exception.getMessage(), status);
    }

    @ExceptionHandler(CannotStartNewChatForDeletedItemException.class)
    public ResponseEntity<ErrorResponse> handleException(
            CannotStartNewChatForDeletedItemException exception) {

        HttpStatus status = HttpStatus.FORBIDDEN;

        return createResponseEntity(exception.getMessage(), status);
    }

    @ExceptionHandler(FavoriteItemAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleException(
            FavoriteItemAlreadyExistsException exception) {

        HttpStatus status = HttpStatus.CONFLICT;

        return createResponseEntity(exception.getMessage(), status);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleException(
            IllegalArgumentException exception) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return createResponseEntity(exception.getMessage(), status);
    }

    @ExceptionHandler(ImageSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleException(
            ImageSizeExceededException exception) {

        return createResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImageSavingException.class)
    public ResponseEntity<ErrorResponse> handleException(ImageSavingException exception) {
        return createResponseEntity(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(String errorMessage, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(errorMessage));
    }

    private String getValidationMessage(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("Fehler...");
    }
}