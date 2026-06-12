package auth;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    protected Lock credentialsLock = new ReentrantLock();

    public AuthManager(LoggingManager logger, DatabaseService databaseService) {
        this.logger = logger;
        this.databaseService = databaseService;
    };

    public abstract void register(User userData) throws AuthException;

    public abstract void authenticate(User userData) throws AuthException;

    public abstract String generatePasswordHash(String password);

    public void setCachedCredentials(User credentials) {
        synchronized(credentialsLock) {
            this.credentials = credentials;
        }
    }

    public User getCachedCredentials() {
        synchronized(credentialsLock) {
            return credentials;
        }
    }

    public void dropCachedCredentials() {
        synchronized(credentialsLock) {
            this.credentials = null;
        }
    }
}
