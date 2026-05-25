package biblioteca.exception;

public class AbonamentExpiredException extends Exception {
    public AbonamentExpiredException() {
        super();
    }

    public AbonamentExpiredException(String message) {
        super(message);
    }

    public AbonamentExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
