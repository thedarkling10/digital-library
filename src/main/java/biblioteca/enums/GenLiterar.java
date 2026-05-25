package biblioteca.enums;

public enum GenLiterar {
    FICTION("Fictiune"),
    NON_FICTION("Non-fictiune"),
    FANTASY("Fantezie"),
    SCIENCE_FICTION("Stiintifico-fantastic"),
    MYSTERY("Mister"),
    BIOGRAPHY("Biografie"),
    HISTORY("Istorie");

    private final String denumire;
    private GenLiterar(String denumire) {
        this.denumire = denumire;
    }

    public String getDenumire() {
        return denumire;
    }
}