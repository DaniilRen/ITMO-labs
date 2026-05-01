package database.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectionPool {
    private static ConnectionPool instance;
    private final BlockingQueue<Connection> pool;
    private final String url;
    private final String user;
    private final String password;
    private final int maxSize;
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    
    private ConnectionPool(String url, String user, String password, int maxSize) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.maxSize = maxSize;
        this.pool = new LinkedBlockingQueue<>(maxSize);
    }
    
    public static void initialize(String url, String user, String password) {
        initialize(url, user, password, 10);
    }
    
    public static void initialize(String url, String user, String password, int maxSize) {
        if (instance == null) {
            instance = new ConnectionPool(url, user, password, maxSize);
            instance.createInitialConnections();
        }
    }
    
    private void createInitialConnections() {
        try {
            for (int i = 0; i < Math.min(5, maxSize); i++) {
                pool.offer(createConnection());
            }
            initialized.set(true);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create initial connections", e);
        }
    }
    
    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    
    public static Connection getConnection() throws SQLException {
        if (instance == null) {
            throw new SQLException("ConnectionPool not initialized. Call initialize() first.");
        }
        
        Connection conn = instance.pool.poll();
        if (conn != null) {
            try {
                if (conn.isClosed() || !conn.isValid(1)) {
                    conn = instance.createConnection();
                }
            } catch (SQLException e) {
                conn = instance.createConnection();
            }
            return conn;
        }
        
        return instance.createConnection();
    }
    
    public static void releaseConnection(Connection conn) {
        if (instance != null && conn != null) {
            try {
                if (!conn.isClosed() && instance.pool.size() < instance.maxSize) {
                    instance.pool.offer(conn);
                } else {
                    conn.close();
                }
            } catch (SQLException e) {
                try {
                    conn.close();
                } catch (SQLException ignored) {}
            }
        }
    }
    
    public static void close() {
        if (instance != null) {
            for (Connection conn : instance.pool) {
                try {
                    conn.close();
                } catch (SQLException ignored) {}
            }
            instance.pool.clear();
            instance = null;
        }
    }
    
    public static int getActiveConnections() {
        return instance != null ? instance.pool.size() : 0;
    }
}