package biblioteca.validator;


public class InputValidator {
    private InputValidator(){

    }

    public static String validareNumeCarte(String numeCarte) {
        if (numeCarte == null || numeCarte.isBlank()) {
            throw new IllegalArgumentException("Numele cartii nu poate fi null!");
        }
        if (numeCarte.matches(".*[@#$%^&*\":{}|<>].*")) {
            throw new IllegalArgumentException("Numele cartii nu poate contine caractere speciale!");
        }
        return numeCarte;
    }

    public static String validareNumePersoana(String nume) {
        if (nume == null || nume.isBlank()) {
            throw new IllegalArgumentException("Numele nu poate fi null sau gol!");
        }
        if (nume.matches(".*[@#$%^&*\":{}|<>].*")) {
            throw new IllegalArgumentException("Numele nu poate contine caractere speciale!");
        }
        if (nume.length() < 2 || nume.length() > 50) {
            throw new IllegalArgumentException("Numele trebuie sa aiba intre 2 si 50 de caractere!");
        }
        if (nume.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Numele nu poate contine cifre!");
        }
        return nume;
    }

    public static String validareEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email-ul nu poate fi null sau blank!");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email-ul nu este valid!");
        }
        return email;
    }

    public static void validareVarsta(int varsta) {
        if (varsta < 0 || varsta > 120) {
            throw new IllegalArgumentException("Varsta invalida: trebuie sa fie intre 0 si 120.");
        }
    }

    public static void validareSalariu(double salariu) {
        if (Double.isNaN(salariu) || salariu < 0.0) {
            throw new IllegalArgumentException("Salariu invalid: trebuie sa fie un numar nenegativ.");
        }
    }

    public static String validareMetodaPlata(String metoda) {
        if (metoda == null || metoda.isBlank()) {
            throw new IllegalArgumentException("Metoda de plata nu poate fi nula sau goala.");
        }
        String m = metoda.trim().toLowerCase();
        if (!(m.equals("card") || m.equals("cash") || m.equals("transfer"))) {
            throw new IllegalArgumentException("Metoda de plata invalida. Optiuni: card, cash, transfer.");
        }
        return m;
    }

    public static void validareRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating invalid: trebuie sa fie intre 1 si 5 stele.");
        }
    }

    public static void validareComentariu(String comentariu) {
        if (comentariu == null) {
            return;
        }
        if (comentariu.length() > 300) {
            throw new IllegalArgumentException("Comentariul nu poate avea mai mult de 300 de caractere.");
        }
    }

    public static void validareISBN(String isbn) {
        if (isbn == null) {
            throw new IllegalArgumentException("ISBN invalid: nu poate fi null.");
        }
        String digits = isbn.replaceAll("[^0-9]", "");
        if (!(digits.length() == 10 || digits.length() == 13)) {
            throw new IllegalArgumentException("ISBN invalid: trebuie sa contina 10 sau 13 cifre (pot fi separate prin '-' sau ' ').");
        }
    }

    public static void validareTelefon(String telefon) {
        if (telefon == null) {
            throw new IllegalArgumentException("Telefon invalid: nu poate fi null.");
        }
        String digits = telefon.replaceAll("[^0-9]", "");
        if (digits.length() < 10 || digits.length() > 13) {
            throw new IllegalArgumentException("Telefon invalid: trebuie sa contina intre 10 si 13 cifre.");
        }
    }

    public static void validareAnPublicare(int an) {
        int currentYear = java.time.Year.now().getValue();
        if (an < 1000 || an > currentYear) {
            throw new IllegalArgumentException(String.format("An publicare invalid: trebuie sa fie intre 1000 si %d.", currentYear));
        }
    }

    public static void validareNrPagini(int nrPagini) {
        if (nrPagini < 1 || nrPagini > 10000) {
            throw new IllegalArgumentException("Numar pagini invalid: trebuie sa fie intre 1 si 10000.");
        }
    }

    public static void validareNrExemplare(int nrExemplare) {
        if (nrExemplare < 1) {
            throw new IllegalArgumentException("Numar exemplare invalid: trebuie sa fie un numar pozitiv.");
        }
    }

    public static void validareAn(int an) {
        int currentYear = java.time.Year.now().getValue();
        if (an < 1500 || an > currentYear) {
            throw new IllegalArgumentException(String.format("An invalid: trebuie sa fie intre 1500 si %d.", currentYear));
        }
    }
}
