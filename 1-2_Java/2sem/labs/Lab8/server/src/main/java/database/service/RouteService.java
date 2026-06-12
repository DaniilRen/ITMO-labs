package database.service;

import database.pool.ConnectionPool;
import database.dao.RouteDAO;
import database.dao.impl.RouteDAOImpl;
import common.models.Route;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RouteService {
    private final RouteDAO routeDAO;
    
    public RouteService() {
        this.routeDAO = new RouteDAOImpl();
    }
    
    public Route getRoute(int id) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            return routeDAO.findById(conn, id);
        } finally {
            if (conn != null) ConnectionPool.releaseConnection(conn);
        }
    }
    
    public List<Route> getAllRoutes() throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            return routeDAO.findAll(conn);
        } finally {
            if (conn != null) ConnectionPool.releaseConnection(conn);
        }
    }
    
    public void saveRoute(Route route) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            conn.setAutoCommit(false);
            routeDAO.insert(conn, route);
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) ConnectionPool.releaseConnection(conn);
        }
    }
    
    public void updateRoute(Route route) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            conn.setAutoCommit(false);
            routeDAO.update(conn, route);
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) ConnectionPool.releaseConnection(conn);
        }
    }
    
    public void deleteRoute(int id) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            conn.setAutoCommit(false);
            routeDAO.delete(conn, id);
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) ConnectionPool.releaseConnection(conn);
        }
    }
}