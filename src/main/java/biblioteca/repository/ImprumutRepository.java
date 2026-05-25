package biblioteca.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import biblioteca.config.DatabaseConnection;
import biblioteca.enums.StatusImprumut;
import biblioteca.model.Imprumut;

public class ImprumutRepository {
    public void create(Imprumut imprumut) {
        String sql = "INSERT INTO imprumuturi (carte_id, cititor_id, data_imprumut, data_scadenta, data_returnare, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, imprumut.getCarteId());
            stmt.setInt(2, imprumut.getCititorId());
            stmt.setDate(3, new java.sql.Date(imprumut.getDataImprumut().getTime()));
            stmt.setDate(4, new java.sql.Date(imprumut.getDataScadenta().getTime()));
            if (imprumut.getDataReturnare() != null) {
                stmt.setDate(5, new java.sql.Date(imprumut.getDataReturnare().getTime()));
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }
            stmt.setString(6, imprumut.getStatus().name());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    imprumut.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating imprumut", e);
        }
    }


    public List<Imprumut> findAll() {
        List<Imprumut> imprumuturi = new ArrayList<>();
        String sql = "SELECT * FROM imprumuturi";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                imprumuturi.add(new Imprumut(
                        rs.getInt("id"),
                        rs.getInt("carte_id"),
                        rs.getInt("cititor_id"),
                        rs.getDate("data_imprumut"),
                        rs.getDate("data_scadenta"),
                        rs.getDate("data_returnare"),
                        StatusImprumut.valueOf(rs.getString("status"))
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding imprumuturi", e);
        }
        return imprumuturi;
    }

    public Imprumut findById(int id) {
        String sql = "SELECT * FROM imprumuturi WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Imprumut(
                            rs.getInt("id"),
                            rs.getInt("carte_id"),
                            rs.getInt("cititor_id"),
                            rs.getDate("data_imprumut"),
                            rs.getDate("data_scadenta"),
                            rs.getDate("data_returnare"),
                            StatusImprumut.valueOf(rs.getString("status"))
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding imprumut by id", e);
        }
        return null;
    }

    public void update(Imprumut imprumut) {
        String sql = "UPDATE imprumuturi SET carte_id = ?, cititor_id = ?, data_imprumut = ?, data_scadenta = ?, data_returnare = ?, status = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, imprumut.getCarteId());
            statement.setInt(2, imprumut.getCititorId());
            statement.setDate(3, new java.sql.Date(imprumut.getDataImprumut().getTime()));
            statement.setDate(4, new java.sql.Date(imprumut.getDataScadenta().getTime()));
            if (imprumut.getDataReturnare() != null) {
                statement.setDate(5, new java.sql.Date(imprumut.getDataReturnare().getTime()));
            } else {
                statement.setNull(5, java.sql.Types.DATE);
            }
            statement.setString(6, imprumut.getStatus().name());
            statement.setInt(7, imprumut.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating imprumut", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM imprumuturi WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting imprumut", e);
        }
    }

    public int countActiveByCititorId(int cititorId) {
        return (int) findAll().stream()
                .filter(imprumut -> imprumut.getCititorId() == cititorId &&
                        (imprumut.getStatus() == StatusImprumut.ACTIVE || imprumut.getStatus() == StatusImprumut.OVERDUE))
                .count();
    }

    public boolean hasActiveLoanForCarte(int cititorId, int carteId) {
        return findAll().stream()
                .anyMatch(imprumut -> imprumut.getCititorId() == cititorId &&
                        imprumut.getCarteId() == carteId &&
                        (imprumut.getStatus() == StatusImprumut.ACTIVE || imprumut.getStatus() == StatusImprumut.OVERDUE));
    }
}
