package biblioteca.model;

import biblioteca.enums.TipAbonament;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Cititor extends Persoana implements Printable {
    private int cartiCitite;
    private int cartiDeCitit;
    private String email;
    private Abonament abonament;
    private Set<Carte> cartiImprumutate;
    private Map<Integer, Integer> cartiRecenzate;

    public Cititor(String nume, String prenume, int varsta, int cartiCitite, int cartiDeCitit, String email) {
        super(nume, prenume, varsta);

        if(cartiCitite < 0){
            throw new IllegalArgumentException("Numarul de carti citite nu poate fi negativ!");
        }
        if(cartiDeCitit < 0){
            throw new IllegalArgumentException("Numarul de carti de citit nu poate fi negativ!");
        }

        this.cartiCitite = cartiCitite;
        this.cartiDeCitit = cartiDeCitit;
        this.email = email;
        this.cartiImprumutate = new HashSet<>();
        this.cartiRecenzate = new HashMap<>();

        int today = LocalDate.now().getYear() * 10000 + LocalDate.now().getMonthValue() * 100 + LocalDate.now().getDayOfMonth();
        int expiry = LocalDate.now().plusMonths(1).getYear() * 10000 + LocalDate.now().plusMonths(1).getMonthValue() * 100 + LocalDate.now().plusMonths(1).getDayOfMonth();
        this.abonament = new Abonament(String.valueOf(getId()), TipAbonament.STANDARD, today, expiry, 2);
    }

    public Cititor(int id, String nume, String prenume, int varsta, int cartiCitite, int cartiDeCitit, String email){
        super(id, nume, prenume, varsta);

        if(cartiCitite < 0){
            throw new IllegalArgumentException("Numarul de carti citite nu poate fi negativ!");
        }
        if(cartiDeCitit < 0){
            throw new IllegalArgumentException("Numarul de carti de citit nu poate fi negativ!");
        }

        this.cartiCitite = cartiCitite;
        this.cartiDeCitit = cartiDeCitit;
        this.email = email;
        this.abonament = null;
        this.cartiImprumutate = new HashSet<>();
        this.cartiRecenzate = new HashMap<>();
    }

    public int getCartiCitite(){
        return cartiCitite;
    }

    public int getCartiDeCitit(){
        return cartiDeCitit;
    }

    public Abonament getAbonament() {
        return abonament;
    }

    public Set<Carte> getCartiImprumutate() {
        return cartiImprumutate;
    }

    public Map<Integer, Integer> getCartiRecenzate() {
        return cartiRecenzate;
    }

    public void adaugaCarteImprumutata(Carte carte) {
        if (carte == null) {
            return;
        }
        cartiImprumutate.add(carte);
    }

    public void returneazaCarte(Carte carte) {
        if (carte == null) {
            return;
        }
        cartiImprumutate.remove(carte);
    }

    public void adaugaRecenzieCarte(int carteId, int stele) {
        if (carteId <= 0) {
            return;
        }
        cartiRecenzate.put(carteId, stele);
    }

    public Integer getRatingPentruCarte(int carteId) {
        return cartiRecenzate.get(carteId);
    }

    public void setCartiCitite(int cartiCitite) {
        if(cartiCitite < 0) {
            throw new IllegalArgumentException("Numarul de carti citite nu poate fi negativ!");
        }
        this.cartiCitite = cartiCitite;
    }

    public void setCartiDeCitit(int cartiDeCitit) {
        if(cartiDeCitit < 0) {
            throw new IllegalArgumentException("Numarul de carti de citit nu poate fi negativ!");
        }
        this.cartiDeCitit = cartiDeCitit;
    }

    public void setAbonament(Abonament abonament) {
        this.abonament = abonament;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String printDetails() {
        return String.format("ID=%d: %s %s, Varsta=%d, Carti citite=%d, Carti de citit=%d, Email=%s, Carti imprumutate active=%d, Recenzii=%d%n",
                getId(), getNume(), getPrenume(), getVarsta(), cartiCitite, cartiDeCitit, email,
                cartiImprumutate.size(), cartiRecenzate.size());
    }
}
