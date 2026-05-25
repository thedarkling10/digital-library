package biblioteca.model;

public class Angajat extends Persoana implements Printable {
    private String functie;
    private double salariu;

    public Angajat(String nume, String prenume, int varsta, String functie, double salariu) {
        super(nume, prenume, varsta);

        if(functie == null || functie.isBlank()){
            throw new IllegalArgumentException("Completati functia!");
        }
        if(salariu <= 0){
            throw new IllegalArgumentException("Salariul trebuie sa fie un numar pozitiv!");
        }
        this.functie = functie;
        this.salariu = salariu;
    }

    public Angajat(int id, String nume, String prenume, int varsta, String functie, double salariu) {
        super(id, nume, prenume, varsta);
        if(functie == null || functie.isBlank()){
            throw new IllegalArgumentException("Completati functia!");
        }
        if(salariu <= 0){
            throw new IllegalArgumentException("Salariul trebuie sa fie un numar pozitiv!");
        }
        this.functie = functie;
        this.salariu = salariu;
    }

    public String getFunctie(){
        return functie;
    }

    public double getSalariu(){
        return salariu;
    }

    public void setFunctie(String functie) {
        if(functie == null || functie.isBlank()){
            throw new IllegalArgumentException("Completati functia!");
        }
        this.functie = functie;
    }

    public void setSalariu(double salariu) {
        if(salariu <= 0){
            throw new IllegalArgumentException("Salariul trebuie sa fie un numar pozitiv!");
        }
        this.salariu = salariu;
    }

    public void marireSalariu(double procent) {
        if(procent < 0) {
            throw new IllegalArgumentException("Procent invalid!");
        }
        this.salariu = this.salariu * (1.0 + procent/100.0);
    }

    @Override
    public String printDetails() {
        return String.format("ID=%d: %s %s, Varsta=%d, Functie=%s, Salariu=%.2f%n",
                getId(), getNume(), getPrenume(), getVarsta(), functie, salariu);
    }
    
}
