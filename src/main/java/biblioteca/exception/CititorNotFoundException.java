package biblioteca.exception;

public class CititorNotFoundException extends Exception {
    public CititorNotFoundException() {
        super();
    }

    public CititorNotFoundException(String message) {
        super(message);
    }

    public CititorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
