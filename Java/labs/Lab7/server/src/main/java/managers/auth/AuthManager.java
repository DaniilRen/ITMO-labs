package managers.auth;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;

import common.exceptions.AuthException;
import util.logging.AbstractLogger;
import managers.database.AbstractDatabaseManager;

/**
 * Менеджер аутентификации и регистрации
 * @author Septyq
 */
public class AuthManager extends AbstractAuthManager {
    private final String pepper;

    public AuthManager(AbstractLogger logger, AbstractDatabaseManager databaseManager, String pepper) {
        super(logger, databaseManager);
        this.pepper = pepper;
    };

    public void authenticate(String name, String password) throws AuthException {
        boolean auth = databaseManager.authenticate(name, generatePasswordHash(password));
        if (auth) {
            logger.info(String.format("authenticated user: %s", name));
            return;
        }
        String authExceptionMessage = String.format("wrong password for user: %s", name);
        logger.error(authExceptionMessage);
        throw new AuthException(authExceptionMessage);
    };

    public void register(String name, String password) throws AuthException {
        String hashedPassword = generatePasswordHash(password);
        boolean registered = databaseManager.register(name, hashedPassword);
        if (registered) {
            logger.info(String.format("registered new user: %s", name));
            return;
        }
        String registExceptionMessage = String.format("cannot register new user: %s", name);
        logger.error(registExceptionMessage);
        throw new AuthException(registExceptionMessage);
    };

    public String generatePasswordHash(String password) {
        return Hashing.sha256()
            .hashString(password + pepper, StandardCharsets.UTF_8)
            .toString();
        }
};

