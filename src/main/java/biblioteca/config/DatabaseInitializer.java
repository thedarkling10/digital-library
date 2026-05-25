package biblioteca.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class DatabaseInitializer {

    private static final Logger logger = AppLogger.getLogger(DatabaseInitializer.class);

    private DatabaseInitializer() {
    }

    public static void initializeDatabase() {
        logger.info("Starting database initialization");

        try (Connection connection = DatabaseConnection.getConnection()) {
            executeSchemaScript(connection);
            logger.info("Database initialized successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Database initialization failed", e);
        }
    }

    public static void resetDatabase() {
        logger.info("Starting database reset");
        try (Connection connection = DatabaseConnection.getConnection()) {
            try (Statement disableFk = connection.createStatement()) {
                disableFk.execute("SET FOREIGN_KEY_CHECKS=0");
            }

            dropTables(connection);
            executeSchemaScript(connection);

            try (Statement enableFk = connection.createStatement()) {
                enableFk.execute("SET FOREIGN_KEY_CHECKS=1");
            }

            logger.info("Database reset successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Database reset failed", e);
        }
    }

    private static void dropTables(Connection connection) {
        String[] tables = {
                "plati_abonamente",
                "abonament",
                "imprumuturi",
                "recenzii",
                "carti",
                "cititori",
                "angajati",
                "autori",
                "editura_genuri",
                "editura_carti",
                "editura_autori",
                "editura"
        };

        for (String table : tables) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("DROP TABLE IF EXISTS " + table);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Unable to drop table " + table, e);
            }
        }
    }

    private static void executeSchemaScript(Connection connection) throws SQLException {
        String sql = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(DatabaseInitializer.class.getClassLoader()
                                .getResourceAsStream("schema.sql")),
                        StandardCharsets.UTF_8
                )
        ).lines().collect(Collectors.joining("\n"));

        String[] statements = sql.split(";");

        for (String statementSql : statements) {
            if (!statementSql.isBlank()) {
                try (Statement statement = connection.createStatement()) {
                    statement.execute(statementSql.trim());
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Eroare: " + statementSql.trim(), e);
                    throw e;
                }
            }
        }
    }

    public static void main(String[] args) {
        resetDatabase();
    }
}