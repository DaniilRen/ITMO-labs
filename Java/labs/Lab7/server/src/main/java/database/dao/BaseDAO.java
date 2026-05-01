package database.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Data access for abstract db table
 * @author Septyq
 */
public interface BaseDAO<T, ID> {
    T findById(Connection conn, ID id) throws SQLException;
    List<T> findAll(Connection conn) throws SQLException;
    void insert(Connection conn, T entity) throws SQLException;
    void update(Connection conn, T entity) throws SQLException;
    void delete(Connection conn, ID id) throws SQLException;
    boolean exists(Connection conn, ID id) throws SQLException;
}