# Biblioteca Vechya — Library Management System

Aplicatie Java de gestiune a unei biblioteci, dezvoltata ca proiect universitar pentru cursul de **Programare Avansata pe Obiecte (PAO)**.

---

## Tehnologii

| | |
|---|---|
| **Limbaj** | Java 17 |
| **Build** | Maven 3 |
| **Baza de date** | MySQL 8 |
| **Driver JDBC** | mysql-connector-j 8.4.0 |
| **Logging** | SLF4J + Logback |

---

## Arhitectura proiectului

Proiectul urmeaza o arhitectura pe **3 niveluri**:

```
biblioteca/
├── app/               # Punct de intrare + meniu interactiv (UI)
│   ├── Main.java
│   └── Meniu.java
│
├── model/             # Entitati (POJO-uri)
│   ├── Persoana.java        (clasa abstracta)
│   ├── Cititor.java         (extends Persoana)
│   ├── Angajat.java         (extends Persoana)
│   ├── Autor.java
│   ├── Carte.java
│   ├── Imprumut.java
│   ├── Abonament.java
│   ├── PlataAbonament.java
│   ├── Recenzie.java
│   └── Editura.java
│
├── repository/        # Acces la baza de date (JDBC)
│   ├── CarteRepository.java
│   ├── CititorRepository.java
│   ├── ImprumutRepository.java
│   ├── AbonamentRepository.java
│   ├── RecenzieRepository.java
│   ├── EdituraRepository.java
│   ├── AutorRepository.java
│   ├── AngajatRepository.java
│   └── PlataAbonamentRepository.java
│
├── service/           # Logica de business
│   ├── ImprumutService.java
│   ├── CititorService.java
│   ├── AngajatService.java
│   ├── EdituraService.java
│   ├── RecenzieService.java
│   ├── AbonamentService.java
│   ├── PlataAbonamentService.java
│   ├── AutorService.java
│   └── CarteService.java
│
├── enums/             # Tipuri enumeratie
│   ├── GenLiterar.java
│   ├── StatusImprumut.java
│   └── TipAbonament.java
│
├── exception/         # Exceptii personalizate
├── validator/         # Validare input utilizator
├── config/            # Conexiune DB, initializare schema
└── util/              # AuditService (singleton), IdGenerator
```

---

## Concepte OOP implementate

| Concept | Unde |
|---|---|
| Clasa abstracta | `Persoana.java` — baza pentru `Cititor` si `Angajat` |
| Mostenire | `Cititor extends Persoana`, `Angajat extends Persoana` |
| Interfete | `Printable` (printDetails()), `Calculable` (calculeazaRecenzie()) |
| Compozitie | `Cititor` contine `Abonament`; `Recenzie` contine `Carte` + `Cititor` |
| Colectii | `HashSet<Carte>` (carti imprumutate), `HashMap<Integer,Integer>` (recenzii), `TreeSet<String>` (genuri/carti editura), `TreeMap<String,Carte>` (carti sortate) |
| Enum cu campuri | `TipAbonament` (cu pret), `StatusImprumut` (cu label), `GenLiterar` |
| Singleton | `AuditService.getInstance()` |
| Streams API | filtrare imprumuturi active, sortare carti dupa gen, afisare carti imprumutate |
| Exceptii custom | `ImprumutLimitExceededException`, `StocEpuizatException`, `AbonamentExpiredException` etc. |
| Comparator static | `Carte.BY_TITLU` — sortare case-insensitive dupa titlu |
| equals / hashCode | `Carte` — necesar pentru corectitudinea `HashSet<Carte>` |

---

## Baza de date

Schema se gaseste in `src/resources/schema.sql` si este aplicata automat la initializarea aplicatiei.

### Tabele principale

| Tabel | Descriere |
|---|---|
| `carti` | Cartile din biblioteca |
| `cititori` | Cititorii inregistrati |
| `angajati` | Angajatii bibliotecii |
| `autori` | Autorii inregistrati |
| `editura` | Editurile (date principale) |
| `editura_genuri` | Genurile publicate de o editura (relatie 1:N) |
| `editura_carti` | Titlurile publicate de o editura (relatie 1:N) |
| `editura_autori` | Autorii colaboratori ai unei edituri (relatie 1:N) |
| `abonamente` | Abonamentele cititorilor |
| `plati_abonamente` | Platile efectuate pentru abonamente |
| `imprumuturi` | Istoricul imprumuturilor |
| `recenzii` | Recenziile cartilor |

---

## Configurare si rulare

### 1. Prerechizite

- Java 17+
- Maven 3.6+
- MySQL 8 rulând local pe portul `3306`

### 2. Creare baza de date si utilizator MySQL

```sql
CREATE DATABASE biblioteca;
CREATE USER 'biblioteca'@'localhost' IDENTIFIED BY 'biblioteca123';
GRANT ALL PRIVILEGES ON biblioteca.* TO 'biblioteca'@'localhost';
FLUSH PRIVILEGES;
```

Credentialele se pot modifica in `src/resources/application.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/biblioteca
db.user=biblioteca
db.password=biblioteca123
```

### 3. Build

```bash
mvn clean package
```

### 4. Rulare

```bash
java -jar target/biblioteca.jar
```

Sau direct din IDE, clasa `biblioteca.app.Main`.

### 5. Initializare baza de date

La prima rulare, intra ca **Administrator** (optiunea `3`) si alege:
- `1` — Initializeaza baza de date (creeaza tabelele)
- `3` — Reseteaza baza de date (drop + recreare, util la dezvoltare)

---

## Meniu interactiv

### Profil Cititor
| Optiune | Actiune |
|---|---|
| 1 | Inregistrare cititor nou |
| 2 | Vizualizare carti disponibile |
| 3 | Sorteaza carti dupa gen literar |
| 4 | Imprumuta o carte |
| 5 | Returneaza o carte |
| 6 | Adauga recenzie pentru o carte |
| 7 | Vizualizare recenzii pentru o carte |
| 8 | Afiseaza recenziile scrise de tine |
| 9 | Plata abonament |
| 10 | Schimba abonament (STANDARD ↔ PREMIUM) |

### Profil Angajat
| Optiune | Actiune |
|---|---|
| 1-3 | Adauga / Actualizeaza / Sterge o carte |
| 4-6 | Adauga / Actualizeaza / Sterge un cititor |
| 7 | Lista cititori |
| 8 | Cauta cititor dupa ID |
| 9 | Afisare plati abonamente |

### Profil Administrator *(parola protejat)*
| Optiune | Actiune |
|---|---|
| 1 | Initializeaza baza de date |
| 2 | Verifica conexiunea la baza de date |
| 3 | Reseteaza baza de date |
| 4 | Adauga autor |
| 5 | Adauga editura |
| 6 | Lista edituri |
| 7-11 | Gestiune angajati (adauga, actualizeaza, sterge, cauta, listeaza) |

---

## Audit

Fiecare actiune relevanta este inregistrata automat in fisierul `audit.csv` din directorul de rulare, in formatul:

```
actiune,timestamp
Imprumut carte: cititor=1, carte=3,2025-05-29T14:32:11.421Z
Adauga cititor: Ion Popescu,2025-05-29T14:33:02.115Z
```

`AuditService` este implementat ca **singleton eager** (`private static final AuditService instance = new AuditService()`) si este apelat din fiecare Service dupa operatiile relevante.

---

## Abonamente

| Tip | Pret | Limita imprumuturi |
|---|---|---|
| STANDARD | 20 RON / luna | 2 carti simultan |
| PREMIUM | 50 RON / luna | 10 carti simultan |

Un cititor nou primeste automat un abonament STANDARD. Imprumutul este blocat daca abonamentul este expirat sau neplatit.
