package biblioteca.service;

import biblioteca.model.Abonament;
import biblioteca.model.PlataAbonament;
import biblioteca.repository.AbonamentRepository;
import biblioteca.repository.PlataAbonamentRepository;
import biblioteca.util.AuditService;

import java.time.LocalDate;

public class PlataAbonamentService {
    private final AbonamentRepository abonamentRepository;
    private final PlataAbonamentRepository plataRepo;

    public PlataAbonamentService() {
        this.abonamentRepository = new AbonamentRepository();
        this.plataRepo = new PlataAbonamentRepository();
    }

    public void proceseazaPlata(int abonamentId, String metoda) {
        Abonament a = abonamentRepository.findById(abonamentId);
        if (a == null) {
            throw new IllegalArgumentException("Abonamentul : " + abonamentId + " nu exista");
        }

        int today = toIntDate(LocalDate.now());
        if (!a.isExpired(today)) {}

        if (metoda == null || metoda.isBlank()) {
            throw new IllegalArgumentException("Metoda de plata invalida");
        }

        double suma = (a.getTip() != null) ? a.getTip().getPrice() : 20.0;

        PlataAbonament plata = new PlataAbonament(abonamentId, suma, today, metoda);
        plataRepo.create(plata);

        LocalDate newExpiry = LocalDate.now().plusMonths(1);
        a.setDataInceput(today);
        a.setDataExpirare(toIntDate(newExpiry));
        abonamentRepository.update(a);

        AuditService.getInstance().logAction("Proceseaza plata abonament: " + abonamentId + ", suma=" + suma + ", metoda=" + metoda);
    }

    public java.util.List<PlataAbonament> getAllPlati() {
        return plataRepo.findAll();
    }

    private int toIntDate(LocalDate date) {
        return date.getYear() * 10000 + date.getMonthValue() * 100 + date.getDayOfMonth();
    }
}
