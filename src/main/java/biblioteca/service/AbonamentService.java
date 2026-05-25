package biblioteca.service;

import java.util.List;

import biblioteca.model.Abonament;
import biblioteca.repository.AbonamentRepository;
import biblioteca.util.AuditService;

public class AbonamentService {
    private final AbonamentRepository abonamentRepository;
    private final AuditService audit = AuditService.getInstance();

    public AbonamentService(AbonamentRepository abonamentRepository) {
        this.abonamentRepository = abonamentRepository;
    }

    public AbonamentService() {
        this.abonamentRepository = new AbonamentRepository();
    }

    public void adaugaAbonament(Abonament abonament) {
        abonamentRepository.create(abonament);
        audit.logAction("Creare abonament: cititor=" + abonament.getCititorId()
                + ", tip=" + abonament.getTip());
    }

    public Abonament getAbonament(int id) {
        return abonamentRepository.findById(id);
    }

    public Abonament getAbonamentByCititorId(int cititorId) {
        return abonamentRepository.findByCititorId(cititorId);
    }

    public List<Abonament> getAllAbonamente() {
        return abonamentRepository.findAll();
    }

    public void updateAbonament(Abonament abonament) {
        abonamentRepository.update(abonament);
        audit.logAction("Actualizeaza abonament: cititor=" + abonament.getCititorId()
                + ", tip=" + abonament.getTip());
    }

    public void deleteAbonament(int id) {
        abonamentRepository.delete(id);
    }

    public boolean isAbonamentValid(int id, int dataActuala) {
        Abonament abonament = abonamentRepository.findById(id);
        if (abonament != null) {
            return dataActuala <= abonament.getDataExpirare();
        }
        return false;
    }
}
