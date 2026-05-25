package biblioteca.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import biblioteca.config.DatabaseConnection;
import biblioteca.model.Editura;

public class EdituraRepository {

    public void create(Editura editura) {
        String sqlEditura = "INSERT INTO editura (nume_editura, an_infiintare, nr_carti_publicate) VALUES (?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                int edituraId;
                try (PreparedStatement stmt = con.prepareStatement(sqlEditura, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, editura.getNumeEditura());
                    stmt.setInt(2, editura.getAnInfiintare());
                    stmt.setInt(3, editura.getNrCartiPublicate());
                    stmt.executeUpdate();
                    try (ResultSet keys = stmt.getGeneratedKeys()) {
                        if (!keys.next()) throw new RuntimeException("Nu s-a generat ID pentru editura.");
                        edituraId = keys.getInt(1);
                        editura.setIdEditura(edituraId);
                    }
                }
                insertGenuri(con, edituraId, editura.getGenuriPublicate());
                insertCarti(con, edituraId, editura.getCartiPublicate());
                insertAutori(con, edituraId, editura.getAutoriColaboratori());
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la crearea editurii.", e);
        }
    }


    public List<Editura> findAll() {
        List<Editura> edituri = new ArrayList<>();
        String sql = "SELECT id_editura, nume_editura, an_infiintare, nr_carti_publicate FROM editura";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                edituri.add(buildEditura(con, rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea editurilor.", e);
        }
        return edituri;
    }


    public Editura findById(int id) {
        String sql = "SELECT id_editura, nume_editura, an_infiintare, nr_carti_publicate FROM editura WHERE id_editura = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return buildEditura(con, rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea editurii cu id=" + id, e);
        }
        return null;
    }


    public void update(Editura editura) {
        String sqlEditura = "UPDATE editura SET nume_editura = ?, an_infiintare = ?, nr_carti_publicate = ? WHERE id_editura = ?";
        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement stmt = con.prepareStatement(sqlEditura)) {
                    stmt.setString(1, editura.getNumeEditura());
                    stmt.setInt(2, editura.getAnInfiintare());
                    stmt.setInt(3, editura.getNrCartiPublicate());
                    stmt.setInt(4, editura.getIdEditura());
                    stmt.executeUpdate();
                }
                deleteSubsidiary(con, "DELETE FROM editura_genuri WHERE editura_id = ?", editura.getIdEditura());
                deleteSubsidiary(con, "DELETE FROM editura_carti WHERE editura_id = ?", editura.getIdEditura());
                deleteSubsidiary(con, "DELETE FROM editura_autori WHERE editura_id = ?", editura.getIdEditura());

                insertGenuri(con, editura.getIdEditura(), editura.getGenuriPublicate());
                insertCarti(con, editura.getIdEditura(), editura.getCartiPublicate());
                insertAutori(con, editura.getIdEditura(), editura.getAutoriColaboratori());
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea editurii.", e);
        }
    }

    public void delete(int id) {
        
        String sql = "DELETE FROM editura WHERE id_editura = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea editurii cu id=" + id, e);
        }
    }


    private Editura buildEditura(Connection con, ResultSet rs) throws SQLException {
        int id = rs.getInt("id_editura");
        return new Editura(
                id,
                rs.getInt("an_infiintare"),
                rs.getInt("nr_carti_publicate"),
                rs.getString("nume_editura"),
                loadGenuri(con, id),
                loadAutori(con, id),
                loadCarti(con, id)
        );
    }

    private TreeSet<String> loadGenuri(Connection con, int edituraId) throws SQLException {
        TreeSet<String> set = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        try (PreparedStatement stmt = con.prepareStatement(
                "SELECT gen FROM editura_genuri WHERE editura_id = ?")) {
            stmt.setInt(1, edituraId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) set.add(rs.getString("gen"));
            }
        }
        return set;
    }

    private TreeSet<String> loadCarti(Connection con, int edituraId) throws SQLException {
        TreeSet<String> set = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        try (PreparedStatement stmt = con.prepareStatement(
                "SELECT titlu_carte FROM editura_carti WHERE editura_id = ?")) {
            stmt.setInt(1, edituraId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) set.add(rs.getString("titlu_carte"));
            }
        }
        return set;
    }

    private List<String> loadAutori(Connection con, int edituraId) throws SQLException {
        List<String> list = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(
                "SELECT autor FROM editura_autori WHERE editura_id = ?")) {
            stmt.setInt(1, edituraId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(rs.getString("autor"));
            }
        }
        return list;
    }

    private void insertGenuri(Connection con, int edituraId, Iterable<String> genuri) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO editura_genuri (editura_id, gen) VALUES (?, ?)")) {
            for (String gen : genuri) {
                stmt.setInt(1, edituraId);
                stmt.setString(2, gen);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void insertCarti(Connection con, int edituraId, Iterable<String> carti) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO editura_carti (editura_id, titlu_carte) VALUES (?, ?)")) {
            for (String titlu : carti) {
                stmt.setInt(1, edituraId);
                stmt.setString(2, titlu);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void insertAutori(Connection con, int edituraId, Iterable<String> autori) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO editura_autori (editura_id, autor) VALUES (?, ?)")) {
            for (String autor : autori) {
                stmt.setInt(1, edituraId);
                stmt.setString(2, autor);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void deleteSubsidiary(Connection con, String sql, int edituraId) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, edituraId);
            stmt.executeUpdate();
        }
    }
}
