/**
 * @author Trung Kien Huynh
 */
package sp.backend.exception;

public class CannotFavorOwnItemException extends RuntimeException {
    public CannotFavorOwnItemException(String message) {
        super(message);
    }
}
