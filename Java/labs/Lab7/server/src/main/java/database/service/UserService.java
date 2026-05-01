package database.service;

import database.pool.ConnectionPool;
import database.dao.UserDAO;
import database.dao.impl.UserDAOImpl;
import common.models.User;
import java.sql.Connection;
import java.sql.SQLException;

public class UserService {
    private final UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAOImpl();
    }
    
    public User getUserById(int id) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            return userDAO.findById(conn, id);
        } finally {
            if (conn != null) ConnectionPool.releaseConnection(conn);
        }
    }
    
    public User getUserByName(String name) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            return userDAO.findByName(conn, name);
        } finally {
            if (conn != null) ConnectionPool.releaseConnection(conn);
        }
    }
    
    public boolean authenticate(String username, String password) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            return userDAO.authenticate(conn, username, password);
        } finally {
            if (conn != null) ConnectionPool.releaseConnection(conn);
        }
    }
    
    public void registerUser(String username, String password) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            conn.setAutoCommit(false);
            User user = new User(username, password);
            userDAO.insert(conn, user);
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) ConnectionPool.releaseConnection(conn);
        }
    }
    
    public boolean userExists(String username) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            return userDAO.findByName(conn, username) != null;
        } finally {
            if (conn != null) ConnectionPool.releaseConnection(conn);
        }
    }
}