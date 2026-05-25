package biblioteca.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biblioteca.config.DatabaseConnection;
import biblioteca.enums.TipAbonament;
import biblioteca.model.Abonament;

public class AbonamentRepository {

    public void create(Abonament abonament) {
        String sql = "INSERT INTO abonament (id_cititor, tip_abonament, data_inceput, data_expirare, limita_imprumuturi) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(abonament.getCititorId()));
            statement.setString(2, abonament.getTip().toString());
            statement.setInt(3, abonament.getDataInceput());
            statement.setInt(4, abonament.getDataExpirare());
            statement.setInt(5, abonament.getLimitaImprumuturi());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating abonament", e);
        }
    }

    public void create(Connection connection, Abonament abonament) throws SQLException {
        String sql = "INSERT INTO abonament (id_cititor, tip_abonament, data_inceput, data_expirare, limita_imprumuturi) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(abonament.getCititorId()));
            statement.setString(2, abonament.getTip().toString());
            statement.setInt(3, abonament.getDataInceput());
            statement.setInt(4, abonament.getDataExpirare());
            statement.setInt(5, abonament.getLimitaImprumuturi());
            statement.executeUpdate();
        }
    }

    public List<Abonament> findAll() {
        List<Abonament> abonamente = new ArrayList<>();
        String sql = "SELECT id_abonament, id_cititor, tip_abonament, data_inceput, data_expirare, limita_imprumuturi FROM abonament";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Abonament abonament = new Abonament();
                abonament.setId(resultSet.getInt("id_abonament"));
                abonament.setCititorId(resultSet.getString("id_cititor"));
                abonament.setTip(TipAbonament.valueOf(resultSet.getString("tip_abonament")));
                abonament.setDataInceput(resultSet.getInt("data_inceput"));
                abonament.setDataExpirare(resultSet.getInt("data_expirare"));
                abonament.setLimitaImprumuturi(resultSet.getInt("limita_imprumuturi"));
                abonamente.add(abonament);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding abonamente", e);
        }
        return abonamente;
    }

    public Abonament findById(int id) {
        String sql = "SELECT id_abonament, id_cititor, tip_abonament, data_inceput, data_expirare, limita_imprumuturi FROM abonament WHERE id_abonament = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Abonament abonament = new Abonament();
                    abonament.setId(resultSet.getInt("id_abonament"));
                    abonament.setCititorId(resultSet.getString("id_cititor"));
                    abonament.setTip(TipAbonament.valueOf(resultSet.getString("tip_abonament")));
                    abonament.setDataInceput(resultSet.getInt("data_inceput"));
                    abonament.setDataExpirare(resultSet.getInt("data_expirare"));
                    abonament.setLimitaImprumuturi(resultSet.getInt("limita_imprumuturi"));
                    return abonament;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding abonament by id", e);
        }
        return null;
    }

    public Abonament findByCititorId(int cititorId) {
        return findAll().stream()
                .filter(abonament -> abonament.getCititorId().equals(String.valueOf(cititorId)))
                .findFirst()
                .orElse(null);
    }

    public void update(Abonament abonament) {
        String sql = "UPDATE abonament SET id_cititor = ?, tip_abonament = ?, data_inceput = ?, data_expirare = ?, limita_imprumuturi = ? WHERE id_abonament = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, abonament.getCititorId());
            statement.setString(2, abonament.getTip().toString());
            statement.setInt(3, abonament.getDataInceput());
            statement.setInt(4, abonament.getDataExpirare());
            statement.setInt(5, abonament.getLimitaImprumuturi());
            statement.setInt(6, abonament.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating abonament", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM abonament WHERE id_abonament = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting abonament", e);
        }
    }
}
