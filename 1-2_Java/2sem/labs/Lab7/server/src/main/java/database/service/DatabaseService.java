package database.service;

import common.models.Route;
import common.models.User;

import java.sql.SQLException;
import java.util.List;

public class DatabaseService {
    private final RouteService routeService;
    private final UserService userService;
    
    public DatabaseService() {
        this.routeService = new RouteService();
        this.userService = new UserService();
    }
    
    public Route getRoute(int id) throws SQLException {
        return routeService.getRoute(id);
    }
    
    public List<Route> getAllRoutes() throws SQLException {
        return routeService.getAllRoutes();
    }
    
    public void saveRoute(Route route) throws SQLException {
        routeService.saveRoute(route);
    }
    
    public void updateRoute(Route route) throws SQLException {
        routeService.updateRoute(route);
    }
    
    public void deleteRoute(int id) throws SQLException {
        routeService.deleteRoute(id);
    }
    
    public void saveAllRoutes(List<Route> routes) throws SQLException {
        for (Route route : routes) {
            routeService.saveRoute(route);
        }
    }
    
    public void updateAllRoutes(List<Route> routes) throws SQLException {
        for (Route route : routes) {
            routeService.updateRoute(route);
        }
    }
    
    public User getUserByName(String name) throws SQLException {
        return userService.getUserByName(name);
    }
    
    public boolean authenticate(String username, String password) throws SQLException {
        return userService.authenticate(username, password);
    }
    
    public void registerUser(String username, String password) throws SQLException {
        userService.registerUser(username, password);
    }
    
    public boolean userExists(String username) throws SQLException {
        return userService.userExists(username);
    }

    public List<Route> getRoutesByUser(String username) throws SQLException {
        if (!userService.userExists(username)) {
            throw new SQLException("User not found: " + username);
        }
        return routeService.getAllRoutes().stream()
            .filter(route -> route.getAuthor().equals(username))
            .toList();
    }
}