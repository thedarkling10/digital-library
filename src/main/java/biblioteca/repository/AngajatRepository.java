package biblioteca.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import biblioteca.config.DatabaseConnection;
import biblioteca.model.Angajat;

public class AngajatRepository {

    public void create(Angajat angajat) {
        String sql = "INSERT INTO angajati (nume, prenume, varsta, functie, salariu) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, angajat.getNume());
            stmt.setString(2, angajat.getPrenume());
            stmt.setInt(3, angajat.getVarsta());
            stmt.setString(4, angajat.getFunctie());
            stmt.setDouble(5, angajat.getSalariu());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    angajat.setSalariu(angajat.getSalariu()); 
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating angajat", e);
        }
    }

    public List<Angajat> findAll() {
        List<Angajat> list = new ArrayList<>();
        String sql = "SELECT id, nume, prenume, varsta, functie, salariu FROM angajati";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Angajat(
                        rs.getInt("id"),
                        rs.getString("nume"),
                        rs.getString("prenume"),
                        rs.getInt("varsta"),
                        rs.getString("functie"),
                        rs.getDouble("salariu")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding angajati", e);
        }
        return list;
    }

    public Angajat findById(int id) {
        String sql = "SELECT id, nume, prenume, varsta, functie, salariu FROM angajati WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Angajat(
                            rs.getInt("id"),
                            rs.getString("nume"),
                            rs.getString("prenume"),
                            rs.getInt("varsta"),
                            rs.getString("functie"),
                            rs.getDouble("salariu")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding angajat by id", e);
        }
        return null;
    }

    public void update(Angajat angajat) {
        String sql = "UPDATE angajati SET nume = ?, prenume = ?, varsta = ?, functie = ?, salariu = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, angajat.getNume());
            stmt.setString(2, angajat.getPrenume());
            stmt.setInt(3, angajat.getVarsta());
            stmt.setString(4, angajat.getFunctie());
            stmt.setDouble(5, angajat.getSalariu());
            stmt.setInt(6, angajat.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating angajat", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM angajati WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting angajat", e);
        }
    }
}
