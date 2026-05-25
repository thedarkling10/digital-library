package biblioteca.service;

import biblioteca.model.Cititor;
import biblioteca.repository.CititorRepository;
import biblioteca.util.AuditService;

import java.util.List;

public class CititorService {
    private final CititorRepository cititorRepository;
    private final AuditService audit = AuditService.getInstance();

    public CititorService(CititorRepository cititorRepository) {
        this.cititorRepository = cititorRepository;
    }

    public CititorService() {
        this.cititorRepository = new CititorRepository();
    }

    public void adaugaCititor(Cititor cititor) {
        cititorRepository.create(cititor);
        audit.logAction("Adauga cititor: " + cititor.getNume() + " " + cititor.getPrenume());
    }

    public Cititor getCititor(int id) {
        Cititor cititor = cititorRepository.findById(id);
        audit.logAction("Cauta cititor dupa ID: " + id);
        return cititor;
    }

    public List<Cititor> getAllCititori() {
        List<Cititor> cititori = cititorRepository.findAll();
        audit.logAction("Listeaza cititorii");
        return cititori;
    }

    public void updateCititor(Cititor cititor) {
        cititorRepository.update(cititor);
        audit.logAction("Actualizeaza cititor: " + cititor.getId());
    }

    public void deleteCititor(int id) {
        cititorRepository.delete(id);
        audit.logAction("Sterge cititor: " + id);
    }
}
