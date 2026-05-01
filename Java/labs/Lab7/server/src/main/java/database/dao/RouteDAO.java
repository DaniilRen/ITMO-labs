package database.dao;

import common.models.Route;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Data access for route db table
 * @author Septyq
 */
public interface RouteDAO extends BaseDAO<Route, Integer> {
    List<Route> findByAuthor(Connection conn, String author) throws SQLException;
    List<Route> findByDistanceGreaterThan(Connection conn, int distance) throws SQLException;
}