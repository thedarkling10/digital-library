package biblioteca.model;

public class Autor extends Persoana implements Printable {
    private int nrCartiScrise;
    private String[] cartiPublicate;

    public Autor(String nume, String prenume, int varsta, int cartiScrise, String[] cartiPublicate) {
        super(nume, prenume, varsta);
        if(cartiScrise <= 0){
            throw new IllegalArgumentException("Un autor are cel putin o carte scrisa!");
        }
        if(cartiPublicate == null || cartiPublicate.length != cartiScrise) {
            throw new IllegalArgumentException("Numarul de carti publicate (" + (cartiPublicate == null ? 0 : cartiPublicate.length) + ") trebuie sa corespunda cu nrCartiScrise (" + cartiScrise + ")!");
        }
        this.cartiPublicate = cartiPublicate;
        this.nrCartiScrise = cartiScrise;
    }

    public Autor(int id, String nume, String prenume, int varsta, int cartiScrise, String[] cartiPublicate){
        super(id, nume, prenume, varsta);
        
        if(cartiScrise <= 0){
            throw new IllegalArgumentException("Un autor are cel putin o carte scrisa!");
        }
        if(cartiPublicate == null || cartiPublicate.length != cartiScrise) {
            throw new IllegalArgumentException("Numarul de carti publicate (" + (cartiPublicate == null ? 0 : cartiPublicate.length) + ") trebuie sa corespunda cu nrCartiScrise (" + cartiScrise + ")!");
        }
        this.cartiPublicate = cartiPublicate;
        this.nrCartiScrise = cartiScrise;
    }

    public int getCartiScrise(){
        return nrCartiScrise;
    }

    public String[] getCartiPublicate(){
        return cartiPublicate;
    }

    @Override
    public String printDetails() {
        return String.format("ID=%d: %s %s, Varsta=%d, Carti scrise=%d%n",
                getId(), getNume(), getPrenume(), getVarsta(), nrCartiScrise);
    }
}
