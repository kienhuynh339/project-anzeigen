/**
 * @author Trung Kien Huynh
 */
package sp.backend.exception;

public class ImageSizeExceededException extends RuntimeException {
    public ImageSizeExceededException(String message) {
        super(message);
    }
}
