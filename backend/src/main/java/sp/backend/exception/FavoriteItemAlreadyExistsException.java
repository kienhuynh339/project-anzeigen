/**
 * @author Trung Kien Huynh
 */
package sp.backend.exception;

public class FavoriteItemAlreadyExistsException extends RuntimeException {
    public FavoriteItemAlreadyExistsException(String message) {
        super(message);
    }
}
