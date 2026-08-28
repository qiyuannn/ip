package kong.exception;

/**
 * Represents an error that can be presented to a Kong user.
 */
public class KongException extends Exception {
    public KongException(String desc) {
        super(desc);
    }
}
