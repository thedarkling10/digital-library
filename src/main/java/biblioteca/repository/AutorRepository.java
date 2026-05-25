package biblioteca.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biblioteca.config.DatabaseConnection;
import biblioteca.model.Autor;

public class AutorRepository {

    public void create(Autor autor) {
        String sql = "INSERT INTO autori (nume, prenume, varsta, nr_carti_scrise, carti_publicate) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, autor.getNume());
            stmt.setString(2, autor.getPrenume());
            stmt.setInt(3, autor.getVarsta());
            stmt.setInt(4, autor.getCartiScrise());
            stmt.setString(5, String.join(",", autor.getCartiPublicate()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating autor", e);
        }
    }

    public List<Autor> findAll() {
        List<Autor> autori = new ArrayList<>();
        String sql = "SELECT * FROM autori";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                autori.add(new Autor(
                    rs.getInt("id"),
                    rs.getString("nume"),
                    rs.getString("prenume"),
                    rs.getInt("varsta"),
                    rs.getInt("nr_carti_scrise"),
                    parseCartiPublicate(rs.getString("carti_publicate"))
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding autori", e);
        }
        return autori;
    }

    public Autor findById(int id) {
        String sql = "SELECT * FROM autori WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Autor(
                        rs.getInt("id"),
                        rs.getString("nume"),
                        rs.getString("prenume"),
                        rs.getInt("varsta"),
                        rs.getInt("nr_carti_scrise"),
                        parseCartiPublicate(rs.getString("carti_publicate"))
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding autor by id", e);
        }
        return null;
    }

    public void update(Autor autor) {
        String sql = "UPDATE autori SET nume = ?, prenume = ?, varsta = ?, nr_carti_scrise = ?, carti_publicate = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, autor.getNume());
            stmt.setString(2, autor.getPrenume());
            stmt.setInt(3, autor.getVarsta());
            stmt.setInt(4, autor.getCartiScrise());
            stmt.setString(5, String.join(",", autor.getCartiPublicate()));
            stmt.setInt(6, autor.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating autor", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM autori WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting autor", e);
        }
    }

    private String[] parseCartiPublicate(String raw) {
        if (raw == null || raw.isBlank()) {
            return new String[0];
        }
        return raw.split(",");
    }
}
