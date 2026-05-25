package biblioteca.exception;

public class LoanOverdueException extends Exception {
    public LoanOverdueException() {
        super();
    }

    public LoanOverdueException(String message) {
        super(message);
    }

    public LoanOverdueException(String message, Throwable cause) {
        super(message, cause);
    }
}
