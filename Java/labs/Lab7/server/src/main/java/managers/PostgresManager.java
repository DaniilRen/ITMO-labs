package managers;

import common.models.Coordinates;
import common.models.Entity;
import common.models.Location2Dimension;
import common.models.Location3Dimension;
import common.models.Route;
import common.exceptions.CollectionLoadException;
import common.exceptions.CollectionWriteException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PostgresManager implements AbstractDatabaseManager {
    
    private final String url;
    private final String user;
    private final String password;
    
    public PostgresManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    
    @Override
    public Collection<Entity> readCollection() throws CollectionLoadException {
        String selectSQL = """
            SELECT 
                r.id, r.name, r.distance, r.creation_date,
                c.x as coord_x, c.y as coord_y,
                l2.x as from_x, l2.y as from_y, l2.name as from_name,
                l3.x as to_x, l3.y as to_y, l3.z as to_z, l3.name as to_name
            FROM route r
            JOIN coordinates c ON r.coordinates_id = c.id
            JOIN location2Dimension l2 ON r.from_location_id = l2.id
            JOIN location3Dimension l3 ON r.to_location_id = l3.id
        """;
        
        List<Entity> routes = new ArrayList<>();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {
            
            while (rs.next()) {
                Route route = new Route(
                    rs.getInt("id"),
                    rs.getString("name"),
                    new Coordinates(rs.getFloat("coord_x"), rs.getLong("coord_y")),
                    rs.getObject("creation_date", LocalDateTime.class),
                    new Location2Dimension(rs.getInt("from_x"), rs.getDouble("from_y"), rs.getString("from_name")),
                    new Location3Dimension(rs.getDouble("to_x"), rs.getDouble("to_y"), rs.getInt("to_z"), rs.getString("to_name")),
                    rs.getInt("distance")
                );
                
                if (route.validate()) {
                    routes.add(route);
                }
            }
            
        } catch (SQLException e) {
            throw new CollectionLoadException("Failed to read collection: " + e.getMessage());
        }
        
        return routes;
    }
    
    @Override
    public void writeCollection(Collection<? extends Entity> collection) throws CollectionWriteException {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("DELETE FROM route");
                    stmt.execute("DELETE FROM coordinates");
                    stmt.execute("DELETE FROM location2Dimension");
                    stmt.execute("DELETE FROM location3Dimension");
                }
                
                for (Entity entity : collection) {
                    if (entity instanceof Route route) {
                        int coordinatesId = insertOrGetCoordinates(conn, route.getCoordinates());
                        int fromLocationId = insertOrGetLocation2D(conn, route.getLocationFrom());
                        int toLocationId = insertOrGetLocation3D(conn, route.getLocationTo());
                        
                        String insertSQL = """
                            INSERT INTO route (name, distance, creation_date, coordinates_id, from_location_id, to_location_id)
                            VALUES (?, ?, ?, ?, ?, ?)
                        """;
                        
                        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                            pstmt.setString(1, route.getName());
                            pstmt.setInt(2, route.getDistance());
                            pstmt.setObject(3, route.getCreationDate());
                            pstmt.setInt(4, coordinatesId);
                            pstmt.setInt(5, fromLocationId);
                            pstmt.setInt(6, toLocationId);
                            pstmt.executeUpdate();
                        }
                    }
                }
                
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
            
        } catch (SQLException e) {
            throw new CollectionWriteException("Failed to write collection: " + e.getMessage());
        }
    }
    
    private int insertOrGetCoordinates(Connection conn, Coordinates coords) throws SQLException {
        String findSQL = "SELECT id FROM coordinates WHERE x = ? AND y = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(findSQL)) {
            pstmt.setDouble(1, coords.getX());
            pstmt.setLong(2, coords.getY());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        
        String insertSQL = "INSERT INTO coordinates (x, y) VALUES (?, ?) RETURNING id";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setDouble(1, coords.getX());
            pstmt.setLong(2, coords.getY());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        
        throw new SQLException("Failed to insert coordinates");
    }
    
    private int insertOrGetLocation2D(Connection conn, Location2Dimension loc) throws SQLException {
        String findSQL = "SELECT id FROM location2Dimension WHERE x = ? AND y = ? AND name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(findSQL)) {
            pstmt.setInt(1, loc.getX());
            pstmt.setDouble(2, loc.getY());
            pstmt.setString(3, loc.getName());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        
        String insertSQL = "INSERT INTO location2Dimension (x, y, name) VALUES (?, ?, ?) RETURNING id";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setInt(1, loc.getX());
            pstmt.setDouble(2, loc.getY());
            pstmt.setString(3, loc.getName());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        
        throw new SQLException("Failed to insert location2Dimension");
    }
    
    private int insertOrGetLocation3D(Connection conn, Location3Dimension loc) throws SQLException {
        String findSQL = "SELECT id FROM location3Dimension WHERE x = ? AND y = ? AND z = ? AND name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(findSQL)) {
            pstmt.setDouble(1, loc.getX());
            pstmt.setDouble(2, loc.getY());
            pstmt.setInt(3, loc.getZ());
            pstmt.setString(4, loc.getName());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        
        String insertSQL = "INSERT INTO location3Dimension (x, y, z, name) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setDouble(1, loc.getX());
            pstmt.setDouble(2, loc.getY());
            pstmt.setInt(3, loc.getZ());
            pstmt.setString(4, loc.getName());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        
        throw new SQLException("Failed to insert location3Dimension");
    }
}