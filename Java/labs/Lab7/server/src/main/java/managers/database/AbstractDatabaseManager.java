package managers.database;

import java.sql.SQLException;
import java.util.Collection;

import common.models.Entity;
import common.models.Route;
import util.database.api.DatabaseHandler;
import common.exceptions.AuthException;
import common.exceptions.CollectionLoadException;
import common.exceptions.CollectionWriteException;


/**
 * Менеджер для операций с базой данных
 * @author Septyq
 */
public abstract class AbstractDatabaseManager {
    protected final DatabaseHandler api;
    protected final String PostgreSQLurl;
    protected final String PostgreSQLuser;
    protected final String PostgreSQLpassword;

    public AbstractDatabaseManager(DatabaseHandler api, String PostgreSQLurl, String PostgreSQLuser, String PostgreSQLpassword) {
        this.api = api;
        this.PostgreSQLurl = PostgreSQLurl;
        this.PostgreSQLuser = PostgreSQLuser;
        this.PostgreSQLpassword = PostgreSQLpassword;
    }

    public boolean register(String user, String password) throws AuthException {
        try {
            return api.register(api.getConnection(PostgreSQLurl, PostgreSQLuser, PostgreSQLpassword), user, password);   
        } catch (SQLException e) {
            throw new AuthException(e.getMessage());
        }
    };

    public boolean authenticate(String user, String password) throws AuthException {
        try {
            return api.authenticate(api.getConnection(PostgreSQLurl, PostgreSQLuser, PostgreSQLpassword), user, password);   
        } catch (SQLException e) {
            throw new AuthException(e.getMessage());
        }
    };

    public int insertEntity(Entity entity) {
        try {
            return api.insertRoute(api.getConnection(PostgreSQLurl, PostgreSQLuser, PostgreSQLpassword), (Route) entity);
        } catch (SQLException e) {
            return -1;
        }
    };

    public int updateEntity(Entity entity, int id) {
        try {
            return api.updateRoute(api.getConnection(PostgreSQLurl, PostgreSQLuser, PostgreSQLpassword), (Route) entity, id);
        } catch (SQLException e) {
            return -1;
        }
    };

    abstract public Collection<Entity> readCollection() throws CollectionLoadException;
    
    abstract public void writeCollection(Collection<? extends Entity> collection) throws CollectionWriteException;
}
