package biblioteca.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import biblioteca.config.DatabaseConnection;
import biblioteca.model.Carte;
import biblioteca.model.Cititor;
import biblioteca.model.Recenzie;

public class RecenzieRepository {
    
    public void create(Recenzie recenzie) {
        String sql = "INSERT INTO recenzii (carte_id, cititor_id, rating, comentariu, contine_spoiler) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, recenzie.getCarte().getId());
            stmt.setInt(2, recenzie.getCititor().getId());
            stmt.setInt(3, recenzie.getNrStele());
            stmt.setString(4, recenzie.getContinut());
            stmt.setBoolean(5, recenzie.isContineSpoiler());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    recenzie.setIdRecenzie(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating recenzie", e);
        }
    }


    public List<Recenzie> findAll() {
        List<Recenzie> recenzii = new ArrayList<>();
        String sql = "SELECT * FROM recenzii";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                recenzii.add(new Recenzie(
                    rs.getInt("id"),
                    rs.getInt("rating"),
                    rs.getString("comentariu"),
                    rs.getBoolean("contine_spoiler"),
                    null,  
                    null   
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding recenzii", e);
        }
        return recenzii;
    }

    public List<Recenzie> findByCarteId(int carteId) {
        CarteRepository carteRepo = new CarteRepository();
        CititorRepository cititorRepo = new CititorRepository();
        Carte carte = carteRepo.findById(carteId);
        return findAll().stream()
                .filter(recenzie -> {
                    try (java.sql.Connection con = DatabaseConnection.getConnection();
                         java.sql.PreparedStatement stmt = con.prepareStatement("SELECT carte_id FROM recenzii WHERE id = ?")) {
                        stmt.setInt(1, recenzie.getIdRecenzie());
                        try (java.sql.ResultSet rs = stmt.executeQuery()) {
                            return rs.next() && rs.getInt("carte_id") == carteId;
                        }
                    } catch (java.sql.SQLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(recenzie -> {
                    Cititor cititor = cititorRepo.findById(getCititorIdFromRecenzie(recenzie.getIdRecenzie()));
                    recenzie.setCarte(carte);
                    recenzie.setCititor(cititor);
                    return recenzie;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    private int getCititorIdFromRecenzie(int recenzieId) {
        String sql = "SELECT cititor_id FROM recenzii WHERE id = ?";
        try (java.sql.Connection con = DatabaseConnection.getConnection();
             java.sql.PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, recenzieId);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cititor_id");
                }
            }
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Error getting cititor id from recenzie", e);
        }
        return 0;
    }

    public Recenzie findByCarteIdAndCititorId(int carteId, int cititorId) {
        String sql = "SELECT * FROM recenzii WHERE carte_id = ? AND cititor_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, carteId);
            stmt.setInt(2, cititorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CarteRepository carteRepo = new CarteRepository();
                    CititorRepository cititorRepo = new CititorRepository();
                    Carte carte = carteRepo.findById(carteId);
                    Cititor cititor = cititorRepo.findById(cititorId);
                    return new Recenzie(
                            rs.getInt("id"),
                            rs.getInt("rating"),
                            rs.getString("comentariu"),
                            rs.getBoolean("contine_spoiler"),
                            carte,
                            cititor
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding recenzie by carte and cititor", e);
        }
        return null;
    }

    public Recenzie findById(int id) {
        String sql = "SELECT * FROM recenzii WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Recenzie(
                        rs.getInt("id"),
                        rs.getInt("rating"),
                        rs.getString("comentariu"),
                        rs.getBoolean("contine_spoiler"),
                        null, 
                        null 
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding recenzie by id", e);
        }
        return null;
    }

    public void update(Recenzie recenzie) {
        String sql = "UPDATE recenzii SET rating = ?, comentariu = ?, contine_spoiler = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, recenzie.getNrStele());
            stmt.setString(2, recenzie.getContinut());
            stmt.setBoolean(3, recenzie.isContineSpoiler());
            stmt.setInt(4, recenzie.getIdRecenzie());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating recenzie", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM recenzii WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting recenzie", e);
        }
    }
}
