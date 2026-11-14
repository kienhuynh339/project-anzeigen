/**
 * @author Trung Kien Huynh
 */
package sp.backend.exception;

public class CannotStartNewChatForDeletedItemException extends RuntimeException {
    public CannotStartNewChatForDeletedItemException(String message) {
        super(message);
    }
}
