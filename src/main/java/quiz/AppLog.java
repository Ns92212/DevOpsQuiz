package quiz;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Journalisation minimale vers stdout/stderr (lisible avec « docker logs »). */
public final class AppLog {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AppLog() { }

    private static String line(String level, String msg) {
        return LocalDateTime.now().format(FMT) + " [" + level + "] " + msg;
    }

    public static void info(String msg)  { System.out.println(line("INFO", msg)); }
    public static void warn(String msg)  { System.out.println(line("WARN", msg)); }
    public static void error(String msg) { System.err.println(line("ERROR", msg)); }
}
