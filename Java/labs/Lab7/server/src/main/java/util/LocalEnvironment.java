package util;
/**
 * позволяет получить переменные среды.
 * @author Septyq
 */
public class LocalEnvironment {
    public static String getDatabaseURL() {
        return System.getenv("DB_URL");
    }

    public static String getDatabaseUser() {
        return System.getenv("DB_USER");
    }

    public static String getDatabasePassword() {
        return System.getenv("DB_PASSWORD");
    }
}
