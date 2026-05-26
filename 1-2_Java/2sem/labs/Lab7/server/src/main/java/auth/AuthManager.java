package auth;

import common.exceptions.AuthException;
import common.models.User;
import database.service.DatabaseService;
import logging.LoggingManager;

/**
 * Абстрактный менеджер аутентификации и регистрации
 * @author Septyq
 */
public abstract class AuthManager {
    protected final LoggingManager logger;
    protected final DatabaseService databaseService;
    protected User credentials;

    public AuthManager(LoggingManager logger, DatabaseService databaseService) {
        this.logger = logger;
        this.databaseService = databaseService;
    };

    public abstract void register(User userData) throws AuthException;

    public abstract void authenticate(User userData) throws AuthException;

    public abstract String generatePasswordHash(String password);

    public void setCachedCredentials(User credentials) {
        // System.out.println(String.format("setting sredentials: name=%s, passw=%s", credentials.getName(), credentials.getPassword()));
        this.credentials = credentials;
    }

    public User getCachedCredentials() {
        return credentials;
    }

    public void dropCachedCredentials() {
        this.credentials = null;
    }
}
