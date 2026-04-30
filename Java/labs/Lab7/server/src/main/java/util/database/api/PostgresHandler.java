package util.database.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.exceptions.AuthException;
import common.models.Coordinates;
import common.models.Location2Dimension;
import common.models.Location3Dimension;
import common.models.Route;
import util.database.table.CoordinatesHandler;
import util.database.table.LocationFromHandler;
import util.database.table.LocationToHandler;

/**
 * Определяет прямое взаимодействия с PostgreSQL
 * @author Septyq
 */
public class PostgresHandler extends DatabaseHandler {
    public Connection getConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public PreparedStatement prepareQuery(Connection connection, String queryMessage) throws SQLException {
        return connection.prepareStatement(queryMessage);
    }

    public boolean authenticate(Connection connection, String user, String password) throws AuthException {
        try {
            return password.equals(getHashedPassword(connection, user));
        } catch (AuthException e) {
            throw new AuthException(e.getMessage());
        }
    };

    public boolean register(Connection connection, String user, String password) throws AuthException {
        if (userExists(connection, user)) throw new AuthException("User already exists! Choose different username");
        try (
            PreparedStatement query = prepareQuery(
              connection, 
              "INSERT INTO users (name, password) VALUES(?, ?) RETURNING id"
            );
        ){
            query.setString(1, user);
            query.setString(2, password);
            ResultSet result = query.executeQuery();
            return result.next();
        } catch (SQLException e) {
            throw new AuthException(e.getMessage());
        }


    }

    private boolean userExists(Connection connection, String user) {
        try(PreparedStatement query = prepareQuery(connection, "SELECT id FROM users WHERE name = ?")) {
            query.setString(1, user);
            ResultSet result = query.executeQuery();
            return result.next();
        } catch (SQLException e) {
            return false;
        }
    }

    private String getHashedPassword(Connection connection, String user) throws AuthException {
        try (PreparedStatement query = prepareQuery(connection, "SELECT password FROM users WHERE name = ?")) {
            query.setString(1, user);
            ResultSet result = query.executeQuery();
            if (!(result.next())) {
                throw new AuthException("No such user");
            }
            return result.getString("password");
        } catch (SQLException e) {
            throw new AuthException(e.getMessage());
        }
    }


    public int getUpdatedCoordinatesId(Connection connection, Coordinates coords) throws SQLException {
        CoordinatesHandler handler = new CoordinatesHandler(connection);
        int existingId = handler.getId(coords);   
        if (existingId < 0) return handler.insert(coords);
        return existingId;
    }

    public int getUpdatedLocationFromId(Connection connection, Location2Dimension location) throws SQLException {
        LocationFromHandler handler = new LocationFromHandler(connection);
        int existingId = handler.getId(location);   
        if (existingId < 0) return handler.insert(location);
        return existingId;
    }

    public int getUpdatedLocationToId(Connection connection, Location3Dimension location) throws SQLException {
        LocationToHandler handler = new LocationToHandler(connection);
        int existingId = handler.getId(location);   
        if (existingId < 0) return handler.insert(location);
        return existingId;
    }
    
    public int insertRoute(Connection connection, Route object) throws SQLException {
        try (PreparedStatement query = prepareQuery(connection, "INSERT INTO route (name, distance, creation_date, coordinates_id, from_location_id, to_location_id, author) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id")) {
            query.setString(1, object.getName());
            query.setInt(2, object.getDistance());
            query.setObject(3, object.getCreationDate());
            query.setInt(4, getUpdatedCoordinatesId(connection, object.getCoordinates()));
            query.setInt(5, getUpdatedLocationFromId(connection, object.getLocationFrom()));
            query.setInt(6, getUpdatedLocationToId(connection, object.getLocationTo()));
            query.setString(7, object.getAuthor());

            ResultSet result = query.executeQuery();

            if (!(result.next())) throw new SQLException("Error while adding entry");
            return result.getInt("id");
        }
    }
}
