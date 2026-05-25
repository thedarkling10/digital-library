package biblioteca.model;

import biblioteca.util.IdGenerator;

public class Recenzie implements Printable{
    private int idRecenzie, nrStele;
    private String continut;
    private boolean contineSpoiler;
    private Carte carte;
    private Cititor cititor;

    public Recenzie(int nrStele, String continut, boolean contineSpoiler, Carte carte, Cititor cititor) {
        this.idRecenzie = IdGenerator.nextId(Recenzie.class);
        this.nrStele = nrStele;
        this.continut = continut;
        this.contineSpoiler = contineSpoiler;
        this.carte = carte;
        this.cititor = cititor;
    }

    public Recenzie(int idRecenzie, int nrStele, String continut, boolean contineSpoiler, Carte carte, Cititor cititor) {
        this.idRecenzie = idRecenzie;
        this.nrStele = nrStele;
        this.continut = continut;
        this.contineSpoiler = contineSpoiler;
        this.carte = carte;
        this.cititor = cititor;
    }

    public Recenzie() {
    }

    public int getIdRecenzie() {
        return idRecenzie;
    }

    public int getNrStele() {
        return nrStele;
    }

    public String getContinut() {
        return continut;
    }

    public boolean isContineSpoiler() {
        return contineSpoiler;
    }

    public Carte getCarte() {
        return carte;
    }

    public Cititor getCititor() {
        return cititor;
    }

    public void setIdRecenzie(int idRecenzie) {
        this.idRecenzie = idRecenzie;
    }

    public void setNrStele(int nrStele) {
        if (nrStele < 1 || nrStele > 5) {
            throw new IllegalArgumentException("Numarul de stele trebuie sa fie intre 1 si 5!");
        }
        this.nrStele = nrStele;
    }

    public void setContinut(String continut) {
        this.continut = continut;
    }

    public void setContineSpoiler(boolean contineSpoiler) {
        this.contineSpoiler = contineSpoiler;
    }

    public void setCarte(Carte carte) {
        this.carte = carte;
    }

    public void setCititor(Cititor cititor) {
        this.cititor = cititor;
    }

    public void calculeazaRecenzie() {
        String titluCarte = (carte != null) ? carte.getTitlu() : "?";
        System.out.println("Recenzie pentru: " + titluCarte + " - Rating: " + nrStele + " stele");
    }

    @Override
    public String printDetails() {
        String titlu = (carte != null) ? carte.getTitlu() : "?";
        String cititorNume = (cititor != null) ? cititor.getNume() : "?";
        return String.format("ID=%d: %d stele, Carte=%s, Cititor=%s, Spoiler=%b%nComentariu: %s%n",
                idRecenzie, nrStele, titlu, cititorNume, contineSpoiler, continut == null ? "" : continut);
    }
}
