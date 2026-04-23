package managers.database;

import java.sql.SQLException;
import java.util.Collection;

import common.models.Entity;
import util.database.handlers.database.DatabaseHandler;
import common.exceptions.AuthException;
import common.exceptions.CollectionLoadException;
import common.exceptions.CollectionWriteException;


/**
 * Менеджер для операций с базой данных
 * @author Septyq
 */
public abstract class AbstractDatabaseManager {
    protected final DatabaseHandler api;
    protected final String url;
    protected final String user;
    protected final String password;

    public AbstractDatabaseManager(DatabaseHandler api, String url, String user, String password) {
        this.api = api;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public boolean register(String user, String password) throws AuthException {
        try {
            return api.register(api.getConnection(url, user, password), user, password);   
        } catch (SQLException e) {
            throw new AuthException(e.getMessage());
        }
    };

    public boolean authenticate(String user, String password) throws AuthException {
        try {
            return api.authenticate(api.getConnection(url, user, password), user, password);   
        } catch (SQLException e) {
            throw new AuthException(e.getMessage());
        }
    };

    abstract public Collection<Entity> readCollection() throws CollectionLoadException;
    
    abstract public void writeCollection(Collection<? extends Entity> collection) throws CollectionWriteException;
}
