package biblioteca.exception;

public class CarteNotFoundException extends Exception {
    public CarteNotFoundException() {
        super();
    }

    public CarteNotFoundException(String message) {
        super(message);
    }

    public CarteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
