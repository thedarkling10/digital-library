package biblioteca.model;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import biblioteca.util.IdGenerator;

public class Editura implements Printable {
    private static final Comparator<String> STRING_COMPARATOR = String.CASE_INSENSITIVE_ORDER;

    private int idEditura;
    private int anInfiintare;
    private int nrCartiPublicate;
    private String numeEditura;
    private List<String> autoriColaboratori;
    private TreeSet<String> cartiPublicate;
    private TreeSet<String> genuriPublicate;

    public Editura(int anInfiintare, int nrCartiPublicate, String numeEditura, TreeSet<String> genuriPublicate, List<String> autoriColaboratori, TreeSet<String> cartiPublicate) {
        this.idEditura = IdGenerator.nextId(Editura.class);
        this.anInfiintare = anInfiintare;
        this.numeEditura = numeEditura;
        this.genuriPublicate = genuriPublicate == null ? new TreeSet<>(STRING_COMPARATOR) : new TreeSet<>(STRING_COMPARATOR);
        if (genuriPublicate != null) this.genuriPublicate.addAll(genuriPublicate);
        this.autoriColaboratori = autoriColaboratori == null ? List.of() : List.copyOf(autoriColaboratori);
        this.cartiPublicate = cartiPublicate == null ? new TreeSet<>(STRING_COMPARATOR) : new TreeSet<>(STRING_COMPARATOR);
        if (cartiPublicate != null) this.cartiPublicate.addAll(cartiPublicate);
        this.nrCartiPublicate = nrCartiPublicate;
        validateCartiCount();
    }

    public Editura(int idEditura, int anInfiintare, int nrCartiPublicate, String numeEditura, TreeSet<String> genuriPublicate, List<String> autoriColaboratori, TreeSet<String> cartiPublicate) {
        this.idEditura = idEditura;
        this.anInfiintare = anInfiintare;
        this.numeEditura = numeEditura;
        this.genuriPublicate = genuriPublicate == null ? new TreeSet<>(STRING_COMPARATOR) : new TreeSet<>(STRING_COMPARATOR);
        if (genuriPublicate != null) this.genuriPublicate.addAll(genuriPublicate);
        this.autoriColaboratori = autoriColaboratori == null ? List.of() : List.copyOf(autoriColaboratori);
        this.cartiPublicate = cartiPublicate == null ? new TreeSet<>(STRING_COMPARATOR) : new TreeSet<>(STRING_COMPARATOR);
        if (cartiPublicate != null) this.cartiPublicate.addAll(cartiPublicate);
        this.nrCartiPublicate = nrCartiPublicate;
        validateCartiCount();
    }

    public int getIdEditura() {
        return idEditura;
    }

    public int getAnInfiintare() {
        return anInfiintare;
    }

    public int getNrCartiPublicate() {
        return nrCartiPublicate;
    }

    public String getNumeEditura() {
        return numeEditura;
    }

    public List<String> getAutoriColaboratori() {
        return autoriColaboratori;
    }

    public TreeSet<String> getCartiPublicate() {
        return cartiPublicate;
    }

    public TreeSet<String> getGenuriPublicate() {
        return genuriPublicate;
    }

    public void setIdEditura(int idEditura) {
        this.idEditura = idEditura;
    }

    public void setAnInfiintare(int anInfiintare) {
        this.anInfiintare = anInfiintare;
    }

    public void setNrCartiPublicate(int nrCartiPublicate) {
        this.nrCartiPublicate = nrCartiPublicate;
    }

    public void setNumeEditura(String numeEditura) {
        this.numeEditura = numeEditura;
    }

    public void setAutoriColaboratori(List<String> autoriColaboratori) {
        this.autoriColaboratori = autoriColaboratori == null ? List.of() : List.copyOf(autoriColaboratori);
    }

    public void setCartiPublicate(TreeSet<String> cartiPublicate) {
        this.cartiPublicate = new TreeSet<>(STRING_COMPARATOR);
        if (cartiPublicate != null) this.cartiPublicate.addAll(cartiPublicate);
        validateCartiCount();
    }

    public void setGenuriPublicate(TreeSet<String> genuriPublicate) {
        this.genuriPublicate = new TreeSet<>(STRING_COMPARATOR);
        if (genuriPublicate != null) this.genuriPublicate.addAll(genuriPublicate);
    }

    @Override
    public String printDetails() {
        return String.format("ID=%d: %s, An infiintare=%d, Nr carti publicate=%d, Genuri=%s, Carti=%s%n",
                idEditura,
                numeEditura,
                anInfiintare,
                nrCartiPublicate,
                genuriPublicate,
                cartiPublicate);
    }

    private void validateCartiCount() {
        if (cartiPublicate == null || cartiPublicate.size() != nrCartiPublicate) {
            throw new IllegalArgumentException("Numarul de carti publicate trebuie sa corespunda cu nrCartiPublicate."
                    + " Asteptate=" + nrCartiPublicate + ", gasite=" + (cartiPublicate == null ? 0 : cartiPublicate.size()));
        }
    }

}
