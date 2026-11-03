package sp.backend.exception;

public class CannotFavoriteOwnItemException extends RuntimeException {
    public CannotFavoriteOwnItemException(String message) {
        super(message);
    }
}
