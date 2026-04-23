package managers.auth;

import common.exceptions.AuthException;
import util.logging.AbstractLogger;
import managers.database.AbstractDatabaseManager;

/**
 * Абстрактный менеджер аутентификации и регистрации
 * @author Septyq
 */
public abstract class AbstractAuthManager {
    protected final AbstractLogger logger;
    protected final AbstractDatabaseManager databaseManager;

    public AbstractAuthManager(AbstractLogger logger, AbstractDatabaseManager databaseManager) {
        this.logger = logger;
        this.databaseManager = databaseManager;
    };

    public abstract void register(String name, String password) throws AuthException;

    public abstract void authenticate(String name, String password) throws AuthException;

    public abstract String generatePasswordHash(String password);
}
