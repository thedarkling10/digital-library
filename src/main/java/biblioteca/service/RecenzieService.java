package biblioteca.service;

import java.util.List;

import biblioteca.model.Carte;
import biblioteca.model.Recenzie;
import biblioteca.repository.RecenzieRepository;
import biblioteca.util.AuditService;

public class RecenzieService {
    private final RecenzieRepository recenzieRepository;
    private final AuditService audit = AuditService.getInstance();

    public RecenzieService(RecenzieRepository recenzieRepository) {
        this.recenzieRepository = recenzieRepository;
    }

    public RecenzieService() {
        this.recenzieRepository = new RecenzieRepository();
    }

    public void adaugaRecenzie(Recenzie recenzie) {
        recenzieRepository.create(recenzie);
        audit.logAction("Adauga recenzie: carte=" + recenzie.getCarte().getId()
                + ", cititor=" + recenzie.getCititor().getId()
                + ", stele=" + recenzie.getNrStele());
    }

    public Recenzie getRecenzie(int id) {
        return recenzieRepository.findById(id);
    }

    public List<Recenzie> getAllRecenzii() {
        return recenzieRepository.findAll();
    }

    public void updateRecenzie(Recenzie recenzie) {
        recenzieRepository.update(recenzie);
    }

    public void deleteRecenzie(int id) {
        recenzieRepository.delete(id);
    }

    public boolean existsRecenzie(int carteId, int cititorId) {
        return recenzieRepository.findByCarteIdAndCititorId(carteId, cititorId) != null;
    }

    public List<Recenzie> getRecenziiByCarte(Carte carte) {
        if (carte == null) {
            throw new IllegalArgumentException("Cartea nu poate fi null.");
        }
        List<Recenzie> recenzii = recenzieRepository.findByCarteId(carte.getId());
        audit.logAction("Vizualizeaza recenzii carte: " + carte.getTitlu());
        return recenzii;
    }
}
