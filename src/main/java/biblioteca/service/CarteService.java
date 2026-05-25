package biblioteca.service;

import biblioteca.model.Carte;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class CarteService {
    private Map<Integer, Carte> carti = new HashMap<>();
    private TreeMap<String, Carte> cartiSortate = new TreeMap<>();

    public void adaugaCarte(Carte carte) {
        carti.put(carte.getId(), carte);
        cartiSortate.put(carte.getTitlu(), carte);
    }

    public Carte getCarte(int id){
        return carti.get(id);
    }

    public void imprumutCarteService(int idCarte) {
        Carte carte = carti.get(idCarte);
        if (carte != null) {
            carte.imprumutCarte();
        } else {
            System.out.println("Cartea cu ID-ul " + idCarte + " nu a fost gasita.");
        }
    }

    public void afiseazaCartiSortate() {
        for (Entry<String, Carte> entry : cartiSortate.entrySet()) {
            System.out.println("Titlu: " + entry.getKey() + ", Autor: " + entry.getValue().getAutor());
        }
    }

}
