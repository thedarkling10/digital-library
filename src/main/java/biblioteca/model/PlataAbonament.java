package biblioteca.model;

public class PlataAbonament implements Calculable, Printable {
    private int id;
    private int abonamentId;
    private double suma;
    private int dataPlata; // YYYYMMDD
    private String metoda;

    public PlataAbonament(int abonamentId, double suma, int dataPlata, String metoda) {
        this.abonamentId = abonamentId;
        this.suma = suma;
        this.dataPlata = dataPlata;
        this.metoda = metoda;
    }

    public PlataAbonament(int id, int abonamentId, double suma, int dataPlata, String metoda) {
        this.id = id;
        this.abonamentId = abonamentId;
        this.suma = suma;
        this.dataPlata = dataPlata;
        this.metoda = metoda;
    }

    public int getId() {
        return id;
    }

    public int getAbonamentId() {
        return abonamentId;
    }

    public double getSuma() {
        return suma;
    }

    public int getDataPlata() {
        return dataPlata;
    }

    public String getMetoda() {
        return metoda;
    }

    @Override
    public double calculeazaRecenzie() {
        return suma;
    }

    @Override
    public String printDetails() {
        return String.format("Plata ID=%d: AbonamentID=%d, Suma=%.2f, Data=%d, Metoda=%s%n",
                id, abonamentId, suma, dataPlata, metoda);
    }
}
