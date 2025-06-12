package pti.softwareentwicklg.SchatzInDb.utils;

public class SqlUtils {
    public static String extractTableName(String sql) {
        try {
            sql = sql.toLowerCase().replaceAll("[\\n\\r]+", " ").trim();
            if (sql.contains(" from ")) {
                String afterFrom = sql.split(" from ")[1];
                String[] parts = afterFrom.trim().split("\\s+|;");
                return parts[0].replaceAll("[^a-zA-Z0-9_]", "");
            }
        } catch (Exception e) {
        }
        return null;
    }
}