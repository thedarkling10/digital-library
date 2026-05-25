package biblioteca.validator;

public class ParolaValidator {


    private static final String PAROLA_ADMIN = "admin123";

    private ParolaValidator() {} 

    public static boolean validateParolaAdmin(String parola) {
        if (parola == null || parola.isBlank()) {
            throw new IllegalArgumentException("Parola nu poate fi goala!");
        }
        return PAROLA_ADMIN.equals(parola); 
    }
}