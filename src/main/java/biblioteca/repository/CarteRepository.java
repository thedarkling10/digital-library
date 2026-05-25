package biblioteca.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import biblioteca.config.DatabaseConnection;
import biblioteca.enums.GenLiterar;
import biblioteca.model.Carte;

public class CarteRepository {

    public void create(Carte carte) {
        String sql = "INSERT INTO carti (titlu, autor, an_publicare, nr_pagini, nr_exemplare, nr_imprumuturi, editura, gen_literar) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, carte.getTitlu());
            stmt.setString(2, carte.getAutor());
            stmt.setInt(3, carte.getAnPublicare());
            stmt.setInt(4, carte.getNrPagini());
            stmt.setInt(5, carte.getNrExemplare());
            stmt.setInt(6, carte.getNrImprumuturi());
            stmt.setString(7, carte.getEditura());
            stmt.setString(8, carte.getGenLiterar().name());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    carte.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating carte", e);
        }
    }

    public List<Carte> findAll() {
        List<Carte> carti = new ArrayList<>();
        String sql = "SELECT * FROM carti";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                carti.add(new Carte(
                    rs.getInt("id"),
                    rs.getInt("an_publicare"),
                    rs.getInt("nr_pagini"),
                    rs.getInt("nr_exemplare"),
                    rs.getInt("nr_imprumuturi"),
                    rs.getString("titlu"),
                    rs.getString("autor"),
                    rs.getString("editura"),
                    rs.getString("gen_literar") != null ? GenLiterar.valueOf(rs.getString("gen_literar")) : null
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding carti", e);
        }
        return carti;
    }

    public List<Carte> findByGenreSorted(GenLiterar genLiterar) {
        return findAll().stream()
                .filter(carte -> carte.getGenLiterar() == genLiterar)
                .sorted(Carte.BY_TITLU)
                .collect(java.util.stream.Collectors.toList());
    }

    public Carte findById(int id) {
        String sql = "SELECT * FROM carti WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Carte(
                        rs.getInt("id"),
                        rs.getInt("an_publicare"),
                        rs.getInt("nr_pagini"),
                        rs.getInt("nr_exemplare"),
                        rs.getInt("nr_imprumuturi"),
                        rs.getString("titlu"),
                        rs.getString("autor"),
                        rs.getString("editura"),
                        GenLiterar.valueOf(rs.getString("gen_literar"))
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding carte by id", e);
        }
        return null;
    }

    public Carte findByTitle(String titlu) {
        String sql = "SELECT * FROM carti WHERE LOWER(titlu) = LOWER(?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, titlu);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Carte(
                        rs.getInt("id"),
                        rs.getInt("an_publicare"),
                        rs.getInt("nr_pagini"),
                        rs.getInt("nr_exemplare"),
                        rs.getInt("nr_imprumuturi"),
                        rs.getString("titlu"),
                        rs.getString("autor"),
                        rs.getString("editura"),
                        GenLiterar.valueOf(rs.getString("gen_literar"))
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding carte by title", e);
        }
        return null;
    }

    public void update(Carte carte) {
        String sql = "UPDATE carti SET titlu = ?, autor = ?, an_publicare = ?, nr_pagini = ?, nr_exemplare = ?, nr_imprumuturi = ?, editura = ?, gen_literar = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, carte.getTitlu());
            stmt.setString(2, carte.getAutor());
            stmt.setInt(3, carte.getAnPublicare());
            stmt.setInt(4, carte.getNrPagini());
            stmt.setInt(5, carte.getNrExemplare());
            stmt.setInt(6, carte.getNrImprumuturi());
            stmt.setString(7, carte.getEditura());
            stmt.setString(8, carte.getGenLiterar().name());
            stmt.setInt(9, carte.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating carte", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM carti WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting carte", e);
        }
    }
}
