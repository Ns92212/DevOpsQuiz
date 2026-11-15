package quiz;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Sauvegarde des scores dans un fichier CSV : date;pseudo;score;total. */
public class ScoreStore {

    public record Entry(LocalDateTime date, String pseudo, int score, int total) { }

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Path file;

    public ScoreStore(Path file) {
        this.file = file;
    }

    static String sanitize(String pseudo) {
        return pseudo.replaceAll("[;\\r\\n]", " ").trim();
    }

    /** @return true si le score a bien été écrit, false sinon (l'erreur est journalisée). */
    public boolean save(String pseudo, int score, int total) {
        try {
            Path parent = file.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            String line = String.join(";", LocalDateTime.now().format(FMT), sanitize(pseudo),
                    String.valueOf(score), String.valueOf(total)) + System.lineSeparator();
            Files.writeString(file, line, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (IOException | RuntimeException e) {
            AppLog.error("Impossible d'enregistrer le score dans " + file + " : " + e);
            return false;
        }
    }

    /** Meilleurs scores, triés par score décroissant puis par date décroissante. */
    public List<Entry> top(int n) {
        List<Entry> entries = new ArrayList<>();
        if (!Files.exists(file)) {
            return entries;
        }
        try {
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;
                String[] p = line.split(";");
                try {
                    entries.add(new Entry(LocalDateTime.parse(p[0], FMT), p[1],
                            Integer.parseInt(p[2]), Integer.parseInt(p[3])));
                } catch (RuntimeException e) {
                    AppLog.warn("Ligne de score ignorée (format invalide) : " + line);
                }
            }
        } catch (IOException e) {
            AppLog.error("Impossible de lire les scores dans " + file + " : " + e);
        }
        entries.sort(Comparator.comparingInt(Entry::score).reversed()
                .thenComparing(Entry::date, Comparator.reverseOrder()));
        return entries.subList(0, Math.min(n, entries.size()));
    }
}
