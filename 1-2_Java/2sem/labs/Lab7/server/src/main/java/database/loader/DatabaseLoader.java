package database.loader;

import database.service.DatabaseService;
import common.models.Entity;
import common.exceptions.CollectionLoadException;
import java.sql.SQLException;
import java.util.Collection;

public class DatabaseLoader<T extends Entity> {
    private final DatabaseService databaseService;
    
    public DatabaseLoader(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }
    
    @SuppressWarnings("unchecked")
    public Collection<T> loadCollection() throws CollectionLoadException {
        try {
            return (Collection<T>) databaseService.getAllRoutes();
        } catch (SQLException e) {
            throw new CollectionLoadException("Failed to load collection from database: " + e.getMessage());
        }
    }
}