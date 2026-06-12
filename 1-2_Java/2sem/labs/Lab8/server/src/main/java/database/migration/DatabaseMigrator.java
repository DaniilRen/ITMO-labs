package database.migration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import database.pool.ConnectionPool;

public final class DatabaseMigrator {
  private DatabaseMigrator() {}

  public static void migrate() {
    Connection conn = null;
    try {
      conn = ConnectionPool.getConnection();
      try (Statement stmt = conn.createStatement()) {
        stmt.execute(
            "ALTER TABLE users ADD COLUMN IF NOT EXISTS is_admin BOOLEAN NOT NULL DEFAULT FALSE");
        stmt.execute("UPDATE users SET is_admin = TRUE WHERE name = 'admin'");
      }
    } catch (SQLException e) {
      throw new RuntimeException("Database migration failed: " + e.getMessage(), e);
    } finally {
      if (conn != null) {
        ConnectionPool.releaseConnection(conn);
      }
    }
  }
}
