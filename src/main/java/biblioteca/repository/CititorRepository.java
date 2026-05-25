package biblioteca.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import biblioteca.config.DatabaseConnection;
import biblioteca.enums.TipAbonament;
import biblioteca.model.Abonament;
import biblioteca.model.Carte;
import biblioteca.model.Cititor;
import biblioteca.repository.AbonamentRepository;
import biblioteca.repository.CarteRepository;
import biblioteca.repository.ImprumutRepository;

public class CititorRepository {

    public void create(Cititor cititor) {
        String sql = "INSERT INTO cititori (nume, prenume, varsta, carti_citite, carti_de_citit, email) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, cititor.getNume());
                stmt.setString(2, cititor.getPrenume());
                stmt.setInt(3, cititor.getVarsta());
                stmt.setInt(4, cititor.getCartiCitite());
                stmt.setInt(5, cititor.getCartiDeCitit());
                stmt.setString(6, cititor.getEmail());
                stmt.executeUpdate();
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        int dbId = keys.getInt(1);
                        AbonamentRepository ar = new AbonamentRepository();
                        Abonament a = cititor.getAbonament();
                        int today = LocalDate.now().getYear() * 10000 + LocalDate.now().getMonthValue() * 100 + LocalDate.now().getDayOfMonth();
                        int expiry = LocalDate.now().plusMonths(1).getYear() * 10000 + LocalDate.now().plusMonths(1).getMonthValue() * 100 + LocalDate.now().plusMonths(1).getDayOfMonth();
                        if (a == null) {
                            a = new Abonament(String.valueOf(dbId), TipAbonament.STANDARD, today, expiry, 2);
                        } else {
                            a.setCititorId(String.valueOf(dbId));
                            a.setDataInceput(today);
                            a.setDataExpirare(expiry);
                        }
                        ar.create(con, a);
                        con.commit();
                        return;
                    }
                }
            }
            con.rollback();
            throw new RuntimeException("Error creating cititor: could not obtain generated id");
        } catch (SQLException e) {
            throw new RuntimeException("Error creating cititor", e);
        }
    }

    public List<Cititor> findAll() {
        List<Cititor> cititori = new ArrayList<>();
        String sql = "SELECT * FROM cititori";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cititor cititor = buildCititorFromResultSet(rs);
                cititori.add(cititor);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding cititori", e);
        }
        return cititori;
    }

    public Cititor findById(int id) {
        String sql = "SELECT * FROM cititori WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cititor cititor = buildCititorFromResultSet(rs);
                    return cititor;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding cititor by id", e);
        }
        return null;
    }

    public void update(Cititor cititor) {
        String sql = "UPDATE cititori SET nume = ?, prenume = ?, varsta = ?, carti_citite = ?, carti_de_citit = ?, email = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, cititor.getNume());
            stmt.setString(2, cititor.getPrenume());
            stmt.setInt(3, cititor.getVarsta());
            stmt.setInt(4, cititor.getCartiCitite());
            stmt.setInt(5, cititor.getCartiDeCitit());
            stmt.setString(6, cititor.getEmail());
            stmt.setInt(7, cititor.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating cititor", e);
        }
    }

    private Cititor buildCititorFromResultSet(ResultSet rs) throws SQLException {
        Cititor cititor = new Cititor(
                rs.getInt("id"),
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getInt("varsta"),
                rs.getInt("carti_citite"),
                rs.getInt("carti_de_citit"),
                rs.getString("email")
        );
        populateCartiRecenzate(cititor);
        populateCartiImprumutate(cititor);
        return cititor;
    }

    private void populateCartiRecenzate(Cititor cititor) {
        String sql = "SELECT carte_id, rating FROM recenzii WHERE cititor_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, cititor.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cititor.adaugaRecenzieCarte(rs.getInt("carte_id"), rs.getInt("rating"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error populating carti recenzate for cititor", e);
        }
    }

    private void populateCartiImprumutate(Cititor cititor) {
        String sql = "SELECT carte_id FROM imprumuturi WHERE cititor_id = ? AND status IN ('ACTIVE','OVERDUE')";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, cititor.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                CarteRepository carteRepo = new CarteRepository();
                while (rs.next()) {
                    Carte carte = carteRepo.findById(rs.getInt("carte_id"));
                    if (carte != null) {
                        cititor.adaugaCarteImprumutata(carte);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error populating carti imprumutate for cititor", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM cititori WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting cititor", e);
        }
    }
}
