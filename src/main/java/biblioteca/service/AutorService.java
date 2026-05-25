package biblioteca.service;

import java.util.List;

import biblioteca.model.Autor;
import biblioteca.repository.AutorRepository;
import biblioteca.util.AuditService;

public class AutorService {
    private final AutorRepository autorRepository;
    private final AuditService audit = AuditService.getInstance();

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public AutorService() {
        this.autorRepository = new AutorRepository();
    }

    public void adaugaAutor(Autor autor) {
        autorRepository.create(autor);
        audit.logAction("Adauga autor: " + autor.getNume() + " " + autor.getPrenume());
    }

    public Autor getAutor(int id) {
        return autorRepository.findById(id);
    }

    public List<Autor> getAllAutori() {
        return autorRepository.findAll();
    }

    public void deleteAutor(int id) {
        autorRepository.delete(id);
    }
}
