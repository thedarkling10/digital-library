package biblioteca.service;

import java.sql.Date;
import java.time.LocalDate;

import biblioteca.enums.StatusImprumut;
import biblioteca.model.Carte;
import biblioteca.model.Cititor;
import biblioteca.model.Imprumut;
import biblioteca.repository.AbonamentRepository;
import biblioteca.repository.CarteRepository;
import biblioteca.repository.CititorRepository;
import biblioteca.repository.ImprumutRepository;
import biblioteca.repository.PlataAbonamentRepository;
import biblioteca.util.AuditService;


public class ImprumutService {
    private final AuditService audit = AuditService.getInstance();
    private ImprumutRepository imprumutRepository;
    private AbonamentRepository abonamentRepository;
    private PlataAbonamentRepository plataRepository;
    private CarteRepository carteRepository;
    private CititorRepository cititorRepository;

    public ImprumutService(ImprumutRepository imprumutRepository) {
        this.imprumutRepository = imprumutRepository;
        this.abonamentRepository = new AbonamentRepository();
        this.plataRepository = new PlataAbonamentRepository();
        this.carteRepository = new CarteRepository();
        this.cititorRepository = new CititorRepository();
    }

    public ImprumutService() {
        this.imprumutRepository = new ImprumutRepository();
        this.abonamentRepository = new AbonamentRepository();
        this.plataRepository = new PlataAbonamentRepository();
        this.carteRepository = new CarteRepository();
        this.cititorRepository = new CititorRepository();
    }

    public void adaugaImprumut(Imprumut imprumut) {
        int cititorId = imprumut.getCititorId();
        var abonament = abonamentRepository.findByCititorId(cititorId);
        if (abonament == null) {
            throw new RuntimeException("Cititorul nu are abonament asociat!");
        }

        int today = toIntDate(LocalDate.now());
        if (abonament.isExpired(today)) {
            throw new RuntimeException("Abonamentul este expirat. Reincarcati abonamentul pentru a putea imprumuta.");
        }
        if(!plataRepository.existsForAbonament(abonament.getId())) {
            throw new RuntimeException("Abonamentul nu este platit. Nu se poate face imprumut.");
        }

        Carte carte = carteRepository.findById(imprumut.getCarteId());
        if(carte == null) {
            throw new RuntimeException("Cartea nu exista.");
        }
        if(carte.getNrExemplare() <= carte.getNrImprumuturi()) {
            throw new RuntimeException("Nu sunt disponibile exemplare ale acestei carti.");
        }
        if(imprumutRepository.hasActiveLoanForCarte(cititorId, carte.getId())) {
            throw new RuntimeException("Cititorul are deja cartea imprumutata.");
        }

        int activeLoans = imprumutRepository.countActiveByCititorId(cititorId);
        if (activeLoans >= abonament.getLimitaImprumuturi()) {
            throw new RuntimeException("Ati atins limita de imprumuturi permisa de abonamentul platit (" + abonament.getLimitaImprumuturi() + "). Retirnati o carte sau schimbati abonamentul.");
        }

        if(imprumut.getStatus() == null) {
            imprumut.setStatus(StatusImprumut.ACTIVE);
        }

        imprumutRepository.create(imprumut);
        audit.logAction("Imprumut carte: cititor=" + imprumut.getCititorId() + ", carte=" + imprumut.getCarteId());
        carte.imprumutCarte();
        carteRepository.update(carte);

        Cititor cititor = cititorRepository.findById(cititorId);
        if (cititor != null) {
            cititor.setCartiDeCitit(cititor.getCartiDeCitit() + 1);
            cititor.adaugaCarteImprumutata(carte);
            cititorRepository.update(cititor);
        }
    }

    public void returneazaImprumut(int imprumutId, int cititorId) {
        Imprumut imprumut = imprumutRepository.findById(imprumutId);
        if(imprumut == null || imprumut.getCititorId() != cititorId) {
            throw new RuntimeException("Imprumutul nu exista sau nu apartine acestui cititor.");
        }
        if(imprumut.getStatus() != StatusImprumut.ACTIVE) {
            throw new RuntimeException("Acest imprumut s-a realizat deja.");
        }
        if(imprumut.getStatus() == StatusImprumut.OVERDUE) {
            throw new RuntimeException("Data de returnare este depasita. Va rugam sa returnati cartea cat mai curand pentru a avea in continuare acces la biblioteca.");
        }

        Date today = Date.valueOf(LocalDate.now());
        imprumut.setDataReturnare(today);
        imprumut.setStatus(StatusImprumut.RETURNED);
        imprumutRepository.update(imprumut);
        audit.logAction("Returneaza carte: cititor=" + cititorId + ", imprumut=" + imprumutId);

        Carte carte = carteRepository.findById(imprumut.getCarteId());
        if(carte != null) {
            carte.returnareCarte();
            carteRepository.update(carte);
        }

        Cititor cititor = cititorRepository.findById(cititorId);
        if(cititor != null) {
            cititor.setCartiDeCitit(Math.max(0, cititor.getCartiDeCitit() - 1));
            cititor.setCartiCitite(cititor.getCartiCitite() + 1);
            if (carte != null) {
                cititor.returneazaCarte(carte);
            }
            cititorRepository.update(cititor);
        }
    }

    private int toIntDate(LocalDate date) {
        return date.getYear() * 10000 + date.getMonthValue() * 100 + date.getDayOfMonth();
    }

    public Imprumut getImprumut(int id) {
        return imprumutRepository.findById(id);
    }

    public java.util.List<Imprumut> getAllImprumuturi() {
        return imprumutRepository.findAll();
    }

    public void updateImprumut(Imprumut imprumut) {
        imprumutRepository.update(imprumut);
    }
}
