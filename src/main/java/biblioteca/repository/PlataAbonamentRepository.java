package biblioteca.repository;

import biblioteca.config.DatabaseConnection;
import biblioteca.model.PlataAbonament;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlataAbonamentRepository {

    public void create(PlataAbonament plata) {
        String sql = "INSERT INTO plati_abonamente (id_abonament, suma, data_plata, metoda) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, plata.getAbonamentId());
            stmt.setDouble(2, plata.getSuma());
            stmt.setInt(3, plata.getDataPlata());
            stmt.setString(4, plata.getMetoda());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating plata abonament", e);
        }
    }

    public List<PlataAbonament> findAll() {
        List<PlataAbonament> lista = new ArrayList<>();
        String sql = "SELECT id, id_abonament, suma, data_plata, metoda FROM plati_abonamente";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new PlataAbonament(
                        rs.getInt("id"),
                        rs.getInt("id_abonament"),
                        rs.getDouble("suma"),
                        rs.getInt("data_plata"),
                        rs.getString("metoda")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding plati abonamente", e);
        }
        return lista;
    }

    public boolean existsForAbonament(int abonamentId) {
        String sql = "SELECT COUNT(1) as c FROM plati_abonamente WHERE id_abonament = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, abonamentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("c") > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking payment existence", e);
        }
        return false;
    }

    public void deleteByAbonamentId(int abonamentId) {
        String sql = "DELETE FROM plati_abonamente WHERE id_abonament = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, abonamentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting payments for abonament", e);
        }
    }
}
