package database.writer;

import database.service.DatabaseService;
import common.models.Entity;
import common.models.Route;
import common.exceptions.CollectionWriteException;
import java.sql.SQLException;
import java.util.Collection;

public class DatabaseWriter {
    private final DatabaseService databaseService;
    
    public DatabaseWriter(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }
    
    public void writeCollection(Collection<? extends Entity> collection) throws CollectionWriteException {
        try {
            for (Entity entity : collection) {
                if (entity instanceof Route) {
                    Route route = (Route) entity;
                    if (route.getId() == 0) {
                        databaseService.saveRoute(route);
                    } else {
                        databaseService.updateRoute(route);
                    }
                }
            }
        } catch (SQLException e) {
            throw new CollectionWriteException("Failed to write collection to database: " + e.getMessage());
        }
    }
    
    public void saveAllRoutes(Collection<Route> routes) throws CollectionWriteException {
        try {
            for (Route route : routes) {
                databaseService.saveRoute(route);
            }
        } catch (SQLException e) {
            throw new CollectionWriteException("Failed to save routes: " + e.getMessage());
        }
    }
    
    public void clearAndSaveAll(Collection<? extends Entity> collection) throws CollectionWriteException {
        try {
            for (Entity entity : collection) {
                if (entity instanceof Route) {
                    Route route = (Route) entity;
                    if (route.getId() == 0) {
                        databaseService.saveRoute(route);
                    } else {
                        databaseService.updateRoute(route);
                    }
                }
            }
        } catch (SQLException e) {
            throw new CollectionWriteException("Failed to sync collection with database: " + e.getMessage());
        }
    }
}