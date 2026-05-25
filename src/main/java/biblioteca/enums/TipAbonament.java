package biblioteca.enums;

public enum TipAbonament {
    STANDARD("Standard", 20.0),
    PREMIUM("Premium", 50.0);

    private final String tip;
    private final double price;

    private TipAbonament(String tip, double price) {
        this.tip = tip;
        this.price = price;
    }

    public String getTip() {
        return tip;
    }

    public double getPrice() {
        return price;
    }
}
