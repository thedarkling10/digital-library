package biblioteca.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public final class AuditService {

    private static final Path AUDIT_FILE = Path.of("audit.csv");
    private static final String HEADER = "actiune,timestamp";
    private static final AuditService instance = new AuditService();

    private AuditService() {}

    public static AuditService getInstance() {
        return instance;
    }

    public void logAction(String action) {
        try {
            if (Files.notExists(AUDIT_FILE)) {
                Files.writeString(AUDIT_FILE, HEADER + System.lineSeparator());
            }
            String record = String.format("%s,%s%s",
                    normalizeForCsv(action),
                    DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
                    System.lineSeparator());
            Files.writeString(AUDIT_FILE, record, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write audit record", e);
        }
    }

    private static String normalizeForCsv(String value) {
        if (value == null) {
            return "";
        }
        String normalized = value.replace("\"", "\"\"");
        if (normalized.contains(",") || normalized.contains("\n")) {
            return "\"" + normalized + "\"";
        }
        return normalized;
    }
}
