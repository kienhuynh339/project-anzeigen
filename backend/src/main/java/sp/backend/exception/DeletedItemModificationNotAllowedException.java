/**
 * @author Trung Kien Huynh
 */
package sp.backend.exception;

public class DeletedItemModificationNotAllowedException extends RuntimeException {
    public DeletedItemModificationNotAllowedException(String message) {
        super(message);
    }
}
