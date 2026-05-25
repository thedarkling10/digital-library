package biblioteca.exception;

public class ImprumutException extends Exception {
    public ImprumutException() {
        super();
    }

    public ImprumutException(String message) {
        super(message);
    }

    public ImprumutException(String message, Throwable cause) {
        super(message, cause);
    }
}
