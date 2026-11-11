package sp.backend.exception;

public class CannotFavorDeletedItemException extends RuntimeException {
    public CannotFavorDeletedItemException(String message) {
        super(message);
    }
}
