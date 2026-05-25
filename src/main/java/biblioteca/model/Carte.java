package biblioteca.model;

import java.util.Comparator;

import biblioteca.enums.GenLiterar;
import biblioteca.util.IdGenerator;

public class Carte implements Calculable, Printable {
    public static final Comparator<Carte> BY_TITLU = Comparator.comparing(Carte::getTitlu, String.CASE_INSENSITIVE_ORDER);

    private int id;
    private int anPublicare;
    private int nrPagini;
    private int nrExemplare;
    private String titlu;
    private String autor;
    private String editura;
    private int nrImprumuturi;
    private GenLiterar genLiterar;

    public Carte(int anPublicare, int nrPagini, int nrExemplare, int nrImprumuturi, String titlu, String autor, String editura, GenLiterar genLiterar) {
        this.id = IdGenerator.nextId(Carte.class);
        this.anPublicare = anPublicare;
        this.nrPagini = nrPagini;
        this.nrExemplare = nrExemplare;
        this.nrImprumuturi = nrImprumuturi;
        this.titlu = titlu;
        this.autor = autor;
        this.editura = editura;
        this.genLiterar = genLiterar;
    }

    public Carte(int id, int anPublicare, int nrPagini, int nrExemplare, int nrImprumuturi, String titlu, String autor, String editura, GenLiterar genLiterar) {
        this.id = id;
        this.anPublicare = anPublicare;
        this.nrPagini = nrPagini;
        this.nrExemplare = nrExemplare;
        this.nrImprumuturi = nrImprumuturi;
        this.titlu = titlu;
        this.autor = autor;
        this.editura = editura;
        this.genLiterar = genLiterar;
    }

    public int getId() {
        return id;
    }
    public int getAnPublicare() {
        return anPublicare;
    }

    public int getNrPagini() {
        return nrPagini;
    }

    public int getNrExemplare() {
        return nrExemplare;
    }

    public String getTitlu() {
        return titlu;
    }

    public String getAutor() {
        return autor;
    }

    public String getEditura() {
        return editura;
    }

    public int getNrImprumuturi() {
        return nrImprumuturi;
    }

    public GenLiterar getGenLiterar() {
        return genLiterar;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void esteInStoc(){
        if (nrExemplare - nrImprumuturi > 0) {
            System.out.println("Cartea este disponibila pentru imprumut.");
        } else {
            System.out.println("Cartea nu este disponibila pentru imprumut.");
        }
    }

    public void imprumutCarte(){
        if (nrExemplare - nrImprumuturi > 0) {
            nrImprumuturi++;
            System.out.println("Cartea a fost imprumutata cu succes.");
        } else {
            System.out.println("Cartea nu este disponibila.");
        }
    }

    public void returnareCarte(){
        if (nrImprumuturi > 0) {
            nrImprumuturi--;
            System.out.println("Cartea a fost returnata cu succes.");
        } else {
            System.out.println("Nu exista imprumuturi pentru aceasta carte.");
        }
    }

    public void actualizareExemplare(int exempl) {
        if (exempl >= 0) {
            nrExemplare = exempl;
            System.out.println("Numarul de exemplare a fost actualizat la: " + nrExemplare);
        } else {
            System.out.println("Numarul de exemplare nu poate fi negativ.");
        }
    }

    public void schimbaEditura(String edit) {
        editura = edit;
        System.out.println("Editura a fost schimbata la: " + editura);
    }

    public void afisareDetalii(){
        System.out.print(printDetails());
    }

    @Override
    public double calculeazaRecenzie() {
        double rating = (nrImprumuturi > 0) ? (double) nrImprumuturi / nrExemplare * 5 : 0;
        System.out.println("Recenzie pentru " + titlu + ": " + String.format("%.2f", rating) + " stele");
        return rating;
    }

    @Override
    public String printDetails() {
        return String.format("Titlu: %s%nAutor: %s%nEditura: %s%nAn publicare: %d%nNumar pagini: %d%nNumar exemplare: %d%nNumar imprumuturi: %d%nGen literar: %s%n",
                titlu, autor, editura, anPublicare, nrPagini, nrExemplare, nrImprumuturi, genLiterar);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carte carte = (Carte) o;
        return id == carte.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
