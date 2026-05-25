package biblioteca.service;

import java.util.List;

import biblioteca.model.Angajat;
import biblioteca.repository.AngajatRepository;
import biblioteca.util.AuditService;

public class AngajatService {
    private final AngajatRepository angajatRepository;
    private final AuditService audit = AuditService.getInstance();

    public AngajatService(AngajatRepository repo) {
        this.angajatRepository = repo;
    }

    public AngajatService() {
        this.angajatRepository = new AngajatRepository();
    }

    public void adaugaAngajat(Angajat a) {
        angajatRepository.create(a);
        audit.logAction("Adauga angajat: " + a.getNume() + " " + a.getPrenume());
    }

    public List<Angajat> getAllAngajati() {
        List<Angajat> angajati = angajatRepository.findAll();
        audit.logAction("Listeaza angajatii");
        return angajati;
    }

    public Angajat getAngajat(int id) {
        Angajat a = angajatRepository.findById(id);
        audit.logAction("Cauta angajat dupa ID: " + id);
        return a;
    }

    public void updateAngajat(Angajat a) {
        angajatRepository.update(a);
        audit.logAction("Actualizeaza angajat: " + a.getId());
    }

    public void deleteAngajat(int id) {
        angajatRepository.delete(id);
        audit.logAction("Sterge angajat: " + id);
    }
}
