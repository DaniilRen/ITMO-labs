package database.dao;

import common.models.User;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Data access for users db table
 * @author Septyq
 */
public interface UserDAO extends BaseDAO<User, Integer> {
    User findByName(Connection conn, String name) throws SQLException;
    boolean authenticate(Connection conn, String name, String password) throws SQLException;
    void changePassword(Connection conn, int userId, String newPassword) throws SQLException;
}