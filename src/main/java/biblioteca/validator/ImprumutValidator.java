package biblioteca.validator;

public class ImprumutValidator {
    private ImprumutValidator() {
    }

        public static int validareDataImprumut(int dataImprumut) {
            if (dataImprumut <= 0) {
                throw new IllegalArgumentException("Data imprumutului trebuie sa fie un numar pozitiv!");
            }
            
            return dataImprumut;
        }
}
