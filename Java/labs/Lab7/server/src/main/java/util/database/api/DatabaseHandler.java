package util.database.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import common.exceptions.AuthException;
import common.models.Coordinates;
import common.models.Location2Dimension;
import common.models.Location3Dimension;
import common.models.Route;

/**
 * Определяет доступные взаимодействия с базой данных
 * @author Septyq
 */
public abstract class DatabaseHandler {
    abstract public Connection getConnection(String url, String user, String password) throws SQLException;

    abstract public PreparedStatement prepareQuery(Connection conncetion, String queryMessage) throws SQLException;

    abstract public boolean register(Connection connection, String user, String password) throws AuthException;
    
    abstract public boolean authenticate(Connection connection, String user, String password) throws AuthException;

    abstract public int getUpdatedLocationFromId(Connection connection, Location2Dimension location) throws SQLException;

    abstract public int getUpdatedLocationToId(Connection connection, Location3Dimension location) throws SQLException;

    abstract public int getUpdatedCoordinatesId(Connection connection, Coordinates coords) throws SQLException;

    abstract public int insertRoute(Connection connection, Route route) throws SQLException;

    abstract public int updateRoute(Connection connection, Route route, int id) throws SQLException;
}
