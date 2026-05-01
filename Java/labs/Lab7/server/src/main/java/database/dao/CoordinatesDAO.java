package database.dao;

import common.models.Coordinates;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Data access for coordinates db table
 * @author Septyq
 */
public interface CoordinatesDAO extends BaseDAO<Coordinates, Integer> {
    Coordinates findByXY(Connection conn, float x, long y) throws SQLException;
    int insertOrGet(Connection conn, Coordinates coords) throws SQLException;
}