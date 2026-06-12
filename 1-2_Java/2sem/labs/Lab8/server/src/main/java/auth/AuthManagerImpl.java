package auth;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import com.google.common.hash.Hashing;

import common.exceptions.AuthException;
import common.models.User;
import database.service.DatabaseService;
import logging.LoggingManager;

/**
 * Менеджер аутентификации и регистрации
 * @author Septyq
 */
public class AuthManagerImpl extends AuthManager {
    private final String pepper;

    public AuthManagerImpl(LoggingManager logger, DatabaseService databaseService, String pepper) {
        super(logger, databaseService);
        this.pepper = pepper;
    };

    public void authenticate(User userData) throws AuthException {
        String name = userData.getName();
        String password = userData.getPassword();
        try {
            boolean auth = databaseService.authenticate(name, generatePasswordHash(password));
            if (!auth) throw new SQLException("auth error");
            logger.info(String.format("authenticated user: %s", name));
        } catch (SQLException e) {
            String authExceptionMessage = String.format("wrong password for user: %s", name);
            logger.error(authExceptionMessage);
            throw new AuthException(authExceptionMessage);
        }
    };

    public void register(User userData) throws AuthException {
        String name = userData.getName();
        String password = userData.getPassword();
        boolean isAdmin = userData.getIsAdmin();
        try {
            if (databaseService.userExists(name)) {
                throw new AuthException(String.format("user already exists: %s", name));
            }
            String hashedPassword = generatePasswordHash(password);
            databaseService.registerUser(name, hashedPassword, isAdmin);
            logger.info(String.format("registered new user: %s", name));
        } catch (SQLException e) {
            String registExceptionMessage =
                    String.format("cannot register new user: %s (%s)", name, e.getMessage());
            logger.error(registExceptionMessage);
            throw new AuthException(registExceptionMessage);
        }
    };

    public String generatePasswordHash(String password) {
        return Hashing.sha256()
            .hashString(password + pepper, StandardCharsets.UTF_8)
            .toString();
        }
};

