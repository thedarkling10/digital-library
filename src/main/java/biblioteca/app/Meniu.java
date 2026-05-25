package biblioteca.app;

import biblioteca.config.DatabaseConnection;
import biblioteca.config.DatabaseInitializer;
import biblioteca.enums.GenLiterar;
import biblioteca.model.Autor;
import biblioteca.model.Carte;
import biblioteca.model.Cititor;
import biblioteca.model.Editura;
import biblioteca.model.Recenzie;
import biblioteca.repository.CarteRepository;
import biblioteca.service.AbonamentService;
import biblioteca.service.AutorService;
import biblioteca.service.CititorService;
import biblioteca.service.EdituraService;
import biblioteca.service.PlataAbonamentService;
import biblioteca.service.RecenzieService;
import biblioteca.model.Angajat;
import biblioteca.service.AngajatService;
import biblioteca.util.AuditService;
import biblioteca.service.ImprumutService;
import biblioteca.validator.InputValidator;
import biblioteca.validator.ParolaValidator;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Meniu {
    private final Scanner scanner = new Scanner(System.in);
    private final CititorService cititorService = new CititorService();
    private final CarteRepository carteRepository = new CarteRepository();
    private final RecenzieService recenzieService = new RecenzieService();
    private final ImprumutService imprumutService = new ImprumutService();
    private final AutorService autorService = new AutorService();
    private final EdituraService edituraService = new EdituraService();
    private final AngajatService angajatService = new AngajatService();
    private final PlataAbonamentService plataService = new PlataAbonamentService();
    private final AbonamentService abonamentService = new AbonamentService();

    public void start() {
        System.out.println("=== Biblioteca Meniu ===");
            printOptions();
            chooseProfile();
    }

    private void printOptions() {

        System.out.println("Alegeti profilul:");
        System.out.println("1. Cititor");
        System.out.println("2. Angajat");
        System.out.println("3. Administrator");
    }
    

    private void chooseProfile() {
        int profile = readInt("Alege profilul (1-3)");
        switch (profile) {
            case 1 -> {
                while (true) {

                    System.out.println("\nBine ati venit la Biblioteca Vechya! Alege dintre optiunile disponibile:");
                    System.out.println("1. Inregistrare cititor nou");
                    System.out.println("2. Vizualizare carti disponibile");
                    System.out.println("3. Sorteaza carti dupa gen literar");
                    System.out.println("4. Imprumuta o carte");
                    System.out.println("5. Returneaza o carte");
                    System.out.println("6. Adauga recenzie pentru o carte");
                    System.out.println("7. Vizualizare recenzii pentru o carte");
                    System.out.println("8. Afiseaza recenziile scrise de un cititor");
                    System.out.println("9. Plata abonament");
                    System.out.println("10. Schimba abonament (standard <-> premium)");
                    System.out.println("0. Iesire");
                    int option = readInt("Alege optiunea:");
                    switch (option) {
                        case 1 -> addReader();
                        case 2 -> listBooks();
                        case 3 -> listBooksByGenre();
                        case 4 -> borrowBook();
                        case 5 -> returnBook();
                        case 6 -> addBookReview();
                        case 7 -> viewReviews();
                        case 8 -> viewReviewedBooks();
                        case 9 -> paySubscription();
                        case 10 -> changeSubscription();
                        case 0 -> {
                            System.out.println("Deconectare .. Ne vedem data viitoare! Pana atunci, citeste o carte buna!");
                            return;
                        }
                        default -> System.out.println("Optiune invalida pentru cititor.");
                    }
                }
            }
            case 2 -> {
                while(true) {
                    System.out.println("\nBine ati venit, angajat! Alege dintre optiunile disponibile:");
                    System.out.println("1. Adauga o carte noua");
                    System.out.println("2. Actualizeaza o carte existenta");
                    System.out.println("3. Sterge o carte");
                    System.out.println("4. Adauga un cititor nou");
                    System.out.println("5. Actualizeaza un cititor existent");
                    System.out.println("6. Sterge un cititor");
                    System.out.println("7. Lista cititori");
                    System.out.println("8. Cauta cititor dupa ID");
                    System.out.println("9. Afisare plati abonamente");
                    System.out.println("0. Iesire");
                    int option = readInt("Alege optiunea:");
                    switch (option) {
                        case 1 -> addBook();
                        case 2 -> updateBook();
                        case 3 -> deleteBook();
                        case 4 -> addReader();
                        case 5 -> updateReader();
                        case 6 -> deleteReader();
                        case 7 -> listReaders();
                        case 8 -> findReaderById();
                        case 9 -> listPayments();
                        case 0 -> {
                            System.out.println("Deconectare .. Ne vedem data viitoare!");
                            return;
                        }
                        default -> System.out.println("Optiune invalida pentru angajat.");
                    }
                }
            }
            case 3 -> {
                int incercari = 3;
                boolean acces = false;

                while (incercari > 0) {
                    String inputParola = readLine("Introduceti parola de administrator");
                    if (ParolaValidator.validateParolaAdmin(inputParola)) {
                        acces = true;
                        break;
                    }
                    incercari--;
                    System.out.println("Parola incorecta. Mai ai " + incercari + " incercari.");
                }

                if (!acces) {
                    System.out.println("Acces blocat.");
                    return;
                }

                while(true){
                    System.out.println("\nBine ati venit, administrator! Alege dintre optiunile disponibile:");
                    System.out.println("1. Initializeaza baza de date");
                    System.out.println("2. Verifica conexiunea la baza de date");
                    System.out.println("3. Reseteaza baza de date");
                    System.out.println("4. Adauga un autor");
                    System.out.println("5. Adauga o editura");
                    System.out.println("6. Lista edituri");
                    System.out.println("7. Adauga un angajat");
                    System.out.println("8. Actualizeaza un angajat");
                    System.out.println("9. Sterge un angajat");
                    System.out.println("10. Cauta angajat dupa ID");
                    System.out.println("11. Lista angajati");
                    System.out.println("0. Iesire");
                    int option = readInt("Alege optiunea:");
                    switch (option) {
                        case 1 -> initializeDatabase();
                        case 2 -> testDatabaseConnection();
                        case 3 -> resetDatabase();
                        case 4 -> addAuthor();
                        case 5 -> addPublisher();
                        case 6 -> listPublishers();
                        case 7 -> addEmployee();
                        case 8 -> updateEmployee();
                        case 9 -> deleteEmployee();
                        case 10 -> findEmployeeById();
                        case 11 -> listEmployees();
                        case 0 -> {
                            System.out.println("Deconectare .. Ne vedem data viitoare!");
                            return;
                        }
                        default -> System.out.println("Optiune invalida pentru administrator.");
                    }
                }
                
            }
            default -> System.out.println("Profil invalid.");
        }
    }
/*
    private void printOptions() {
        System.out.println("\nOptiuni disponibile:");
        System.out.println("1. Initializeaza baza de date");
        System.out.println("2. Verifica conexiunea la baza de date");
        System.out.println("3. Adauga un cititor");
        System.out.println("4. Actualizeaza un cititor");
        System.out.println("5. Sterge un cititor");
        System.out.println("6. Adauga o carte");
        System.out.println("7. Actualizeaza o carte");
        System.out.println("8. Sterge o carte");
        System.out.println("9. Lista toti cititorii");
        System.out.println("10. Lista toate cartile");
        System.out.println("11. Gaseste un cititor dupa ID");
        System.out.println("12. Gaseste o carte dupa ID");
        System.out.println("13. Adauga un angajat");
        System.out.println("14. Actualizeaza un angajat");
        System.out.println("15. Sterge un angajat");
        System.out.println("16. Lista toti angajatii");
        System.out.println("17. Gaseste un angajat dupa ID");
        System.out.println("18. Plata abonament");
        System.out.println("19. Lista plati abonamente");
        System.out.println("20. Schimba abonament (standard <-> premium)");
        System.out.println("21. Adauga o recenzie pentru o carte");
        System.out.println("22. Adauga un autor");
        System.out.println("23. Adauga o editura");
        System.out.println("24. Lista edituri");
        System.out.println("25. Lista carti dupa gen sortate");
        System.out.println("26. Reseteaza baza de date");
        System.out.println("0. Iesire");
    }
*/
    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                String line = scanner.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Valoare invalida. Introduceti un numar intreg.");
            }
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }

    private void initializeDatabase() {
        System.out.println("Initializare baza de date...");
        DatabaseInitializer.initializeDatabase();
        AuditService.getInstance().logAction("Initializare baza de date");
        System.out.println("Initializare reusita!");
    }

    private void testDatabaseConnection() {
        System.out.println("Verific conexiunea la baza de date...");
        try (Connection connection = DatabaseConnection.getConnection()) {
            System.out.println("Conexiune reusita la baza de date:");
            AuditService.getInstance().logAction("Verificare conexiune cu baza de date");
        } catch (SQLException e) {
            System.out.println("Eroare la conectarea la baza de date: " + e.getMessage());
        }
    }

    private void resetDatabase() {
        System.out.println("Resetez baza de date...");
        DatabaseInitializer.resetDatabase();
        AuditService.getInstance().logAction("Reseteaza baza de date");
        System.out.println("Resetare reusita!");
    }

    // metode cititor
    private void addBookReview() {
        System.out.println("Adauga o recenzie pentru o carte");
        int cititorId = readInt("ID-ul cititorului");
        Cititor cititor = cititorService.getCititor(cititorId);
        if (cititor == null) {
            System.out.println("Cititorul nu a fost gasit.");
            return;
        }

        String titluCarte = InputValidator.validareNumeCarte(readLine("Numele cartii"));
        var carte = carteRepository.findByTitle(titluCarte);
        if (carte == null) {
            System.out.println("Cartea nu a fost gasita.");
            return;
        }

        int stele;
        while (true) {
            try {
                stele = readInt("Numar de stele (1-5)");
                InputValidator.validareRating(stele);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Eroare: " + e.getMessage() + " Introduceti din nou.");
            }
        }

        String continut = readLine("Comentariu (optional, max 300 caractere)");
        try {
            InputValidator.validareComentariu(continut);
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare: " + e.getMessage() + " Comentariul va fi gol.");
            continut = "";
        }

        boolean contineSpoiler = false;
        String spoilerInput = readLine("Contine spoiler? (da/nu)").trim().toLowerCase();
        if (spoilerInput.equals("da") || spoilerInput.equals("yes") || spoilerInput.equals("y")) {
            contineSpoiler = true;
        }

        if (recenzieService.existsRecenzie(carte.getId(), cititorId)) {
            System.out.println("Ai deja o recenzie pentru aceasta carte.");
            return;
        }
        Recenzie recenzie = new Recenzie(stele, continut, contineSpoiler, carte, cititor);
        recenzieService.adaugaRecenzie(recenzie);
        cititor.adaugaRecenzieCarte(carte.getId(), stele);
        cititorService.updateCititor(cititor);
        System.out.println("Recenzia a fost adaugata.");
    }

    private void viewReviewedBooks() {
        System.out.println("Recenziile scrise de cititor");
        int cititorId = readInt("ID-ul cititorului");
        Cititor cititor = cititorService.getCititor(cititorId);
        if (cititor == null) {
            System.out.println("Cititorul nu a fost gasit.");
            return;
        }

        Map<Integer, Integer> recenzii = cititor.getCartiRecenzate();
        if (recenzii.isEmpty()) {
            System.out.println("Nu exista recenzii scrise de acest cititor.");
            return;
        }

        System.out.println("Recenziile scrise:");
        for (Map.Entry<Integer, Integer> intrare : recenzii.entrySet()) {
            var carte = carteRepository.findById(intrare.getKey());
            String titlu = carte != null ? carte.getTitlu() : "ID-ul: " + intrare.getKey() + " nu a fost gasit";
            System.out.printf("- %s (ID=%d): %d stele%n", titlu, intrare.getKey(), intrare.getValue());
        }
    }

    private void viewReviews() {
        System.out.println("Vizualizare recenzii pentru o carte");
        String titluCarte = InputValidator.validareNumeCarte(readLine("Numele cartii"));
        var carte = carteRepository.findByTitle(titluCarte);
        if (carte == null) {
            System.out.println("Cartea nu a fost gasita.");
            return;
        }

        List<Recenzie> recenzii = recenzieService.getRecenziiByCarte(carte);
        if (recenzii.isEmpty()) {
            System.out.println("Nu exista recenzii pentru aceasta carte.");
            return;
        }

        System.out.println("Recenzii pentru " + titluCarte + ":");
        for (Recenzie recenzie : recenzii) {
            String spoilerText = recenzie.isContineSpoiler() ? " (contine spoiler)" : "";
            System.out.printf("- %d stele%s: %s%n", recenzie.getNrStele(), spoilerText, recenzie.getContinut());
        }
    }

    private void borrowBook() {
        System.out.println("Imprumut carte");
        int cititorId = readInt("ID-ul cititorului");
        Cititor cititor = cititorService.getCititor(cititorId);
        if (cititor == null) {
            System.out.println("Cititorul nu a fost gasit.");
            return;
        }

        String titluCarte = InputValidator.validareNumeCarte(readLine("Titlu carte"));
        var carte = carteRepository.findByTitle(titluCarte);
        if (carte == null) {
            System.out.println("Cartea nu a fost gasita.");
            return;
        }

        if (cititor.getCartiImprumutate().contains(carte)) {
            System.out.println("Ai deja imprumutata aceasta carte: " + carte.getTitlu() + ". Nu o poti imprumuta din nou pana nu o returnezi.");
            return;
        }

        try {
            java.sql.Date today = java.sql.Date.valueOf(LocalDate.now());
            java.sql.Date dueDate = java.sql.Date.valueOf(LocalDate.now().plusWeeks(2));
            biblioteca.model.Imprumut imprumut = new biblioteca.model.Imprumut(
                    carte.getId(), cititorId, today, dueDate, null, biblioteca.enums.StatusImprumut.ACTIVE);
            imprumutService.adaugaImprumut(imprumut);
            cititor.adaugaCarteImprumutata(carte);
            cititorService.updateCititor(cititor);
            System.out.println("Imprumutul a fost inregistrat. Data scadenta: " + dueDate);
            System.out.println("Cartile imprumutate curente: " + cititor.getCartiImprumutate().stream().map(Carte::getTitlu).collect(Collectors.joining(", ") ));
        } catch (RuntimeException e) {
            System.out.println("Nu se poate face imprumutul: " + e.getMessage());
            if (e.getMessage().contains("limita de imprumuturi")) {
                Set<Carte> cartiImprumutate = cititor.getCartiImprumutate();
                if (!cartiImprumutate.isEmpty()) {
                    System.out.println("\nCartile pe care le ai imprumutate in prezent:");
                    cartiImprumutate.stream()
                            .map(Carte::getTitlu)
                            .forEach(titlu -> System.out.println("  - " + titlu));
                } else {
                    System.out.println("Nu ai carti imprumutate in prezent.");
                }
            }
        }
    }


    private void returnBook() {
        System.out.println("Returneaza o carte");
        int cititorId = readInt("ID-ul cititorului");
        Cititor cititor = cititorService.getCititor(cititorId);
        if (cititor == null) {
            System.out.println("Cititorul nu a fost gasit.");
            return;
        }

        int imprumutId = readInt("ID-ul imprumutului");
        try {
            imprumutService.returneazaImprumut(imprumutId, cititorId);
            Cititor actualizat = cititorService.getCititor(cititorId);
            System.out.println("Returnarea a fost inregistrata cu succes.");
            if (actualizat != null) {
                System.out.println("Cartile imprumutate curente: " + actualizat.getCartiImprumutate().stream().map(Carte::getTitlu).collect(Collectors.joining(", ") ));
            }
        } catch (RuntimeException e) {
            System.out.println("Nu se poate returna cartea: " + e.getMessage());
        }
    }

    private void addAuthor() {
        System.out.println("Adauga un autor nou");
        String nume;
        String prenume;
        int varsta;

        while (true) {
            try {
                nume = InputValidator.validareNumePersoana(readLine("Nume"));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Eroare: " + e.getMessage() + " Introduceti din nou.");
            }
        }

        while (true) {
            try {
                prenume = InputValidator.validareNumePersoana(readLine("Prenume"));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Eroare: " + e.getMessage() + " Introduceti din nou.");
            }
        }

        while (true) {
            try {
                varsta = readInt("Varsta");
                InputValidator.validareVarsta(varsta);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Eroare: " + e.getMessage() + " Introduceti din nou.");
            }
        }

        int nrCartiScrise;
        while (true) {
            try {
                nrCartiScrise = readInt("Numar de carti scrise");
                if (nrCartiScrise <= 0) {
                    throw new IllegalArgumentException("Un autor trebuie sa aiba cel putin o carte scrisa!");
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Eroare: " + e.getMessage() + " Introduceti din nou.");
            }
        }

        String[] cartiPublicate = new String[nrCartiScrise];
        for (int i = 0; i < nrCartiScrise; i++) {
            cartiPublicate[i] = InputValidator.validareNumeCarte(readLine("Carte scrisa #" + (i + 1)));
        }

        try {
            Autor autor = new Autor(nume, prenume, varsta, nrCartiScrise, cartiPublicate);
            autorService.adaugaAutor(autor);
            System.out.println("Autorul a fost adaugat in baza de date.");
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare la creare autor: " + e.getMessage());
        }
    }

    private void addPublisher() {
        System.out.println("Adauga o editura noua");
        String numeEditura;
        int anInfiintare;
        int nrCartiPublicate;

        while (true) {
            try {
                numeEditura = InputValidator.validareNumePersoana(readLine("Nume editura"));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Eroare: " + e.getMessage() + " Introduceti din nou.");
            }
        }

        while (true) {
            try {
                anInfiintare = readInt("An infiintare");
                InputValidator.validareAn(anInfiintare);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Eroare: " + e.getMessage() + " Introduceti din nou.");
            }
        }

        while (true) {
            try {
                nrCartiPublicate = readInt("Numar de carti publicate");
                if (nrCartiPublicate < 0) {
                    throw new IllegalArgumentException("Numarul de carti publicate nu poate fi negativ.");
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Eroare: " + e.getMessage() + " Introduceti din nou.");
            }
        }


        TreeSet<String> cartiPublicate = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < nrCartiPublicate; i++) {
            cartiPublicate.add(InputValidator.validareNumeCarte(readLine("Titlu carte publicata #" + (i + 1))));
        }

        int nrGenuri;
        while (true) {
            try {
                nrGenuri = readInt("Numar de genuri publicate");
                if (nrGenuri <= 0) {
                    throw new IllegalArgumentException("O editura trebuie sa publice cel putin un gen literar.");
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Eroare: " + e.getMessage() + " Introduceti din nou.");
            }
        }

        TreeSet<String> genuriPublicate = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < nrGenuri; i++) {
            genuriPublicate.add(InputValidator.validareNumePersoana(readLine("Gen literar publicat #" + (i + 1))));
        }

        int nrAutori;
        while (true) {
            try {
                nrAutori = readInt("Numar de autori colaboratori");
                if (nrAutori < 0) {
                    throw new IllegalArgumentException("Numarul de autori colaboratori nu poate fi negativ.");
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Eroare: " + e.getMessage() + " Introduceti din nou.");
            }
        }

        List<String> autoriColaboratori = new ArrayList<>();
        for (int i = 0; i < nrAutori; i++) {
            autoriColaboratori.add(InputValidator.validareNumePersoana(readLine("Numele autorului colaborator #" + (i + 1))));
        }

        try {
            Editura editura = new Editura(anInfiintare, nrCartiPublicate, numeEditura, genuriPublicate, autoriColaboratori, cartiPublicate);
            edituraService.adaugaEditura(editura);
            System.out.println("Editura a fost adaugata in baza de date.");
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare la creare editura: " + e.getMessage());
        }
    }

    private void listPublishers() {
        System.out.println("Lista edituri existente:");
        List<Editura> edituri = edituraService.getAllEdituri();
        if (edituri.isEmpty()) {
            System.out.println("Nu exista edituri inca.");
            return;
        }

        for (Editura editura : edituri) {
            System.out.println(editura.printDetails());
        }
    }

    private void addReader() {
        System.out.println("Adauga un cititor nou");
        String nume;
        String prenume;
        int varsta;
        String email;

        while (true) {
            try {
                nume = InputValidator.validareNumePersoana(readLine("Nume"));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Eroare: " + e.getMessage() + " Introduceti din nou.");
            }
        }

        while (true) {
            try {
                prenume = InputValidator.validareNumePersoana(readLine("Prenume"));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Eroare: " + e.getMessage() + " Introduceti din nou.");
            }
        }

        while (true) {
            try {
                varsta = readInt("Varsta");
                InputValidator.validareVarsta(varsta);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Eroare: " + e.getMessage() + " Introduceti din nou.");
            }
        }

        while (true) {
            try {
                email = InputValidator.validareEmail(readLine("Email"));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Eroare: " + e.getMessage() + " Introduceti din nou.");
            }
        }

        try {
            Cititor cititor = new Cititor(nume, prenume, varsta, 0, 0, email);
            cititorService.adaugaCititor(cititor);
            System.out.println("Cititorul a fost adaugat in baza de date.");
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare la creare cititor: " + e.getMessage());
        }
    }

    private void addBook() {
        System.out.println("Adauga o carte noua");
        String titlu = InputValidator.validareNumeCarte(readLine("Titlu"));
        String autor = InputValidator.validareNumeCarte(readLine("Autor"));
        int anPublicare = readInt("An publicare");
        InputValidator.validareAnPublicare(anPublicare);
        int nrPagini = readInt("Numar pagini");
        InputValidator.validareNrPagini(nrPagini);
        int nrExemplare = readInt("Numar exemplare");
        InputValidator.validareNrExemplare(nrExemplare);
        String editura = InputValidator.validareNumeCarte(readLine("Editura"));
        GenLiterar gen = readGenre();

        Carte carte = new Carte(anPublicare, nrPagini, nrExemplare, 0, titlu, autor, editura, gen);
        carteRepository.create(carte);
        AuditService.getInstance().logAction("Adauga carte: " + titlu + " de " + autor);
        System.out.println("Cartea a fost adaugata in baza de date.");
    }

    private void updateReader() {
        int id = readInt("Introduceti ID-ul cititorului de actualizat");
        Cititor existing = cititorService.getCititor(id);
        if (existing == null) {
            System.out.println("Cititorul nu a fost gasit.");
            return;
        }

        String nume = InputValidator.validareNumeCarte(readLine("Nume nou"));
        String prenume = InputValidator.validareNumeCarte(readLine("Prenume nou"));
        int varsta = readInt("Varsta noua");
        InputValidator.validareVarsta(varsta);
        String email = InputValidator.validareEmail(readLine("Email nou"));

        Cititor update = new Cititor(id, nume, prenume, varsta, existing.getCartiCitite(), existing.getCartiDeCitit(), email);
        cititorService.updateCititor(update);
        System.out.println("Cititorul a fost actualizat.");
    }

    private void deleteReader() {
        int id = readInt("Introdu ID-ul cititorului de sters");
        if (cititorService.getCititor(id) == null) {
            System.out.println("Cititorul nu a fost gasit.");
            return;
        }
        cititorService.deleteCititor(id);
        System.out.println("Cititorul a fost sters cu succes.");
    }

    private void updateBook() {
        int id = readInt("Introdu ID-ul cartii de actualizat");
        Carte existing = carteRepository.findById(id);
        if (existing == null) {
            System.out.println("Cartea nu a fost gasita.");
            return;
        }

        String titlu = InputValidator.validareNumeCarte(readLine("Titlu nou"));
        String autor = InputValidator.validareNumeCarte(readLine("Autor nou"));
        int anPublicare = readInt("An publicare nou");
        InputValidator.validareAnPublicare(anPublicare);
        int nrPagini = readInt("Numar pagini nou");
        InputValidator.validareNrPagini(nrPagini);
        int nrExemplare = readInt("Numar exemplare nou");
        InputValidator.validareNrExemplare(nrExemplare);
        String editura = InputValidator.validareNumeCarte(readLine("Editura noua"));
        GenLiterar gen = readGenre();

        Carte updated = new Carte(id, anPublicare, nrPagini, nrExemplare, existing.getNrImprumuturi(), titlu, autor, editura, gen);
        carteRepository.update(updated);
        AuditService.getInstance().logAction("Actualizeaza carte: " + id);
        System.out.println("Cartea a fost actualizata.");
    }

    private void deleteBook() {
        int id = readInt("Introduceti ID-ul cartii de sters");
        if (carteRepository.findById(id) == null) {
            System.out.println("Cartea nu a fost gasita.");
            return;
        }
        carteRepository.delete(id);
        AuditService.getInstance().logAction("Sterge carte: " + id);
        System.out.println("Cartea a fost stearsa cu succes.");
    }

    private GenLiterar readGenre() {
        String value = readLine("Gen literar (FICTION, NON_FICTION, FANTASY, SCIENCE_FICTION, MYSTERY, BIOGRAPHY, HISTORY)");
        try {
            return GenLiterar.valueOf(value.trim().toUpperCase().replace(' ', '_'));
        } catch (IllegalArgumentException e) {
            System.out.println("Genul literar ales nu este recunoscut, FICTION va fi folosit ca valoare implicita.");
            return GenLiterar.FICTION;
        }
    }

    private void listReaders() {
        System.out.println("Cititori inregistrati:");
        List<Cititor> cititori = cititorService.getAllCititori();
        if (cititori.isEmpty()) {
            System.out.println("Nu exista cititori in baza de date.");
            return;
        }
        for (Cititor cititor : cititori) {
            System.out.printf("ID=%d: %s %s, Varsta=%d, Email=%s%n",
                    cititor.getId(), cititor.getNume(), cititor.getPrenume(), cititor.getVarsta(), cititor.getEmail());
        }
    }

    private void listBooks() {
        System.out.println("Carti disponibile in baza de date:");
        List<Carte> carti = carteRepository.findAll();
        if (carti.isEmpty()) {
            System.out.println("Nu exista carti gasite.");
            return;
        }
        AuditService.getInstance().logAction("Listeaza cartile");
        for (Carte carte : carti) {
            System.out.printf("ID=%d: %s de %s (%d), editura=%s, exmpl=%d%n gen=%s%n",
                    carte.getId(), carte.getTitlu(), carte.getAutor(), carte.getAnPublicare(), carte.getEditura(), carte.getNrExemplare(), carte.getGenLiterar());
        }
    }

    private void listBooksByGenre() {
        System.out.println("Alege genul literar pentru a filtra si sorta cartile:");
        GenLiterar gen = readGenre();
        List<Carte> carti = carteRepository.findByGenreSorted(gen);
        AuditService.getInstance().logAction("Listeaza cartile dupa gen sortate: " + gen);
        if (carti.isEmpty()) {
            System.out.println("Nu exista carti pentru genul " + gen + ".");
            return;
        }
        System.out.println("Carti din genul " + gen + " sortate alfabetic dupa titlu:");
        for (Carte carte : carti) {
            System.out.printf("ID=%d: %s de %s, Editura=%s, An=%d, Gen=%s%n",
                    carte.getId(), carte.getTitlu(), carte.getAutor(), carte.getEditura(), carte.getAnPublicare(), carte.getGenLiterar());
        }
    }

    private void findReaderById() {
        int id = readInt("Introdu ID-ul cititorului");
        Cititor cititor = cititorService.getCititor(id);
        if (cititor == null) {
            System.out.println("Cititorul nu a fost gasit.");
            return;
        }
        System.out.printf("Cititor gasit: %s %s, Varsta=%d, Email=%s%n",
                cititor.getNume(), cititor.getPrenume(), cititor.getVarsta(), cititor.getEmail());
    }

    private void findBookById() {
        int id = readInt("Introdu ID-ul cartii");
        Carte carte = carteRepository.findById(id);
        if (carte == null) {
            System.out.println("Cartea nu a fost gasita.");
            return;
        }
        AuditService.getInstance().logAction("Gaseste carte dupa ID: " + id);
        carte.afisareDetalii();
    }

    private void addEmployee() {
        System.out.println("Adauga un angajat nou");
        String nume = InputValidator.validareNumeCarte(readLine("Nume"));
        String prenume = InputValidator.validareNumeCarte(readLine("Prenume"));
        int varsta = readInt("Varsta");
        InputValidator.validareVarsta(varsta);
        String functie = InputValidator.validareNumeCarte(readLine("Functie"));
        double salariu = Double.parseDouble(readLine("Salariu"));
        InputValidator.validareSalariu(salariu);

        Angajat a = new Angajat(nume, prenume, varsta, functie, salariu);
        angajatService.adaugaAngajat(a);
        System.out.println("Angajatul a fost adaugat.");
    }

    private void updateEmployee() {
        int id = readInt("Introduceti ID-ul angajatului pe care doriti sa il actualizati: ");
        Angajat existing = angajatService.getAngajat(id);
        if (existing == null) {
            System.out.println("Angajatul nu a fost gasit.");
            return;
        }
        String nume = InputValidator.validareNumeCarte(readLine("Nume nou"));
        String prenume = InputValidator.validareNumeCarte(readLine("Prenume nou"));
        int varsta = readInt("Varsta noua");
        InputValidator.validareVarsta(varsta);
        String functie = InputValidator.validareNumeCarte(readLine("Functie noua"));
        double salariu = Double.parseDouble(readLine("Salariu nou"));
        InputValidator.validareSalariu(salariu);

        Angajat updated = new Angajat(id, nume, prenume, varsta, functie, salariu);
        angajatService.updateAngajat(updated);
        System.out.println("Angajatul a fost actualizat.");
    }

    private void deleteEmployee() {
        int id = readInt("Ati ales Sterge angajat. Introduceti ID-ul angajatului: ");
        angajatService.deleteAngajat(id);
        if (angajatService.getAngajat(id) == null) {
            System.out.println("Angajatul a fost sters cu succes.");
        } else {
            System.out.println("Angajatul nu a fost gasit.");
        }
    }

    private void listEmployees() {
        System.out.println("Angajati inregistrati:");
        java.util.List<Angajat> list = angajatService.getAllAngajati();
        if (list.isEmpty()) {
            System.out.println("Nu exista angajati in baza de date.");
            return;
        }
        for (Angajat a : list) {
            System.out.print(a.printDetails());
        }
    }

    private void findEmployeeById() {
        int id = readInt("Introdu ID-ul angajatului");
        Angajat a = angajatService.getAngajat(id);
        if (a == null) {
            System.out.println("Angajatul nu a fost gasit.");
            return;
        }
        System.out.print(a.printDetails());
    }

    private void paySubscription() {
        System.out.println("Plata abonament");
        int id = readInt("Introdu ID-ul abonamentului de platit");
        String metoda = readLine("Metoda plata (card/cash)");
        try {
            plataService.proceseazaPlata(id, metoda);
            System.out.println("Plata a fost procesata cu succes.");
        } catch (Exception e) {
            System.out.println("Eroare la procesarea platii: " + e.getMessage());
        }
    }

    private void listPayments() {
        System.out.println("Plati abonamente:");
        var list = plataService.getAllPlati();
        if (list.isEmpty()) {
            System.out.println("Nu exista plati inregistrate.");
            return;
        }
        for (var p : list) {
            System.out.print(p.printDetails());
        }
    }

    private void changeSubscription() {
        System.out.println("Ati ales schimbarea abonamentului dvs.");
        int cititorId = readInt("Introduceti ID-ul cititorului: ");
        var cititor = cititorService.getCititor(cititorId);
        if (cititor == null) {
            System.out.println("Cititorul nu a fost gasit.");
            return;
        }

        var abonament = abonamentService.getAllAbonamente().stream()
                .filter(a -> a.getCititorId() != null && a.getCititorId().equals(String.valueOf(cititorId)))
                .findFirst().orElse(null);

        if (abonament == null) {
            System.out.println("Cititorul nu are un abonament. Se va crea unul Standard implicit.");
            createDefaultAbonamentForCititor(cititor);
            return;
        }

        System.out.println("Abonament curent: " + (abonament.getTip() != null ? abonament.getTip().name() : "-"));
        String target = readLine("Tip nou (STANDARD/PREMIUM)").trim().toUpperCase();
        if (!(target.equals("STANDARD") || target.equals("PREMIUM"))) {
            System.out.println("Tip invalid.");
            return;
        }
        if (abonament.getTip() != null && abonament.getTip().name().equals(target)) {
            System.out.println("Abonamentul dvs. este deja de tipul solicitat.");
            return;
        }

        int abonamentId = abonament.getId();
        var plataRepo = new biblioteca.repository.PlataAbonamentRepository();
        plataRepo.deleteByAbonamentId(abonamentId);

        String metoda = InputValidator.validareMetodaPlata(readLine("Metoda plata pentru noul abonament (card/cash/transfer)"));
        double suma = (target.equals("PREMIUM")) ? biblioteca.enums.TipAbonament.PREMIUM.getPrice() : biblioteca.enums.TipAbonament.STANDARD.getPrice();
        var plata = new biblioteca.model.PlataAbonament(abonamentId, suma, java.time.LocalDate.now().getYear()*10000 + java.time.LocalDate.now().getMonthValue()*100 + java.time.LocalDate.now().getDayOfMonth(), metoda);
        plataRepo.create(plata);

        abonament.setTip(biblioteca.enums.TipAbonament.valueOf(target));
        abonament.setDataInceput(java.time.LocalDate.now().getYear()*10000 + java.time.LocalDate.now().getMonthValue()*100 + java.time.LocalDate.now().getDayOfMonth());
        abonament.setDataExpirare((int) (java.time.LocalDate.now().plusMonths(1).getYear()*10000 + java.time.LocalDate.now().plusMonths(1).getMonthValue()*100 + java.time.LocalDate.now().plusMonths(1).getDayOfMonth()));
        abonament.setLimitaImprumuturi(target.equals("PREMIUM") ? 10 : 2);
        abonamentService.updateAbonament(abonament);
        System.out.println("Abonamentul a fost schimbat si plata inregistrata, avand suma de " + suma + " RON");
    }

    private void createDefaultAbonamentForCititor(biblioteca.model.Cititor cititor) {
        var a = new biblioteca.model.Abonament(String.valueOf(cititor.getId()), biblioteca.enums.TipAbonament.STANDARD,
                java.time.LocalDate.now().getYear()*10000 + java.time.LocalDate.now().getMonthValue()*100 + java.time.LocalDate.now().getDayOfMonth(),
                (int)(java.time.LocalDate.now().plusMonths(1).getYear()*10000 + java.time.LocalDate.now().plusMonths(1).getMonthValue()*100 + java.time.LocalDate.now().plusMonths(1).getDayOfMonth()),
                2);
        abonamentService.adaugaAbonament(a);
        cititor.setAbonament(a);
        System.out.println("Abonament STANDARD creat (termen 1 luna pentru realizarea platii acestuia).");
    }
}
