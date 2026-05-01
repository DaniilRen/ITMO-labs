package auth;

import common.exceptions.AuthException;
import database.service.DatabaseService;
import logging.LoggingManager;

/**
 * Абстрактный менеджер аутентификации и регистрации
 * @author Septyq
 */
public abstract class AuthManager {
    protected final LoggingManager logger;
    protected final DatabaseService databaseService;

    public AuthManager(LoggingManager logger, DatabaseService databaseService) {
        this.logger = logger;
        this.databaseService = databaseService;
    };

    public abstract void register(String name, String password) throws AuthException;

    public abstract void authenticate(String name, String password) throws AuthException;

    public abstract String generatePasswordHash(String password);
}
