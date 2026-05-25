package biblioteca.service;

import java.util.List;

import biblioteca.model.Editura;
import biblioteca.repository.EdituraRepository;
import biblioteca.util.AuditService;

public class EdituraService {
    private final EdituraRepository edituraRepository;
    private final AuditService audit = AuditService.getInstance();

    public EdituraService(EdituraRepository edituraRepository) {
        this.edituraRepository = edituraRepository;
    }

    public EdituraService() {
        this.edituraRepository = new EdituraRepository();
    }

    public void adaugaEditura(Editura editura) {
        edituraRepository.create(editura);
        audit.logAction("Adauga editura: " + editura.getNumeEditura());
    }

    public Editura getEditura(int id) {
        return edituraRepository.findById(id);
    }

    public List<Editura> getAllEdituri() {
        return edituraRepository.findAll();
    }

    public void updateEditura(Editura editura) {
        edituraRepository.update(editura);
        audit.logAction("Actualizeaza editura: " + editura.getIdEditura());
    }

    public void deleteEditura(int id) {
        edituraRepository.delete(id);
        audit.logAction("Sterge editura: " + id);
    }
}
