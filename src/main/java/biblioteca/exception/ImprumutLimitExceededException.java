package biblioteca.exception;

public class ImprumutLimitExceededException extends Exception {
    public ImprumutLimitExceededException() {
        super();
    }

    public ImprumutLimitExceededException(String message) {
        super(message);
    }

    public ImprumutLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
