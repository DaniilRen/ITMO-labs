package managers;

import logging.LoggerBlueprint;

public abstract class AuthManagerBlueprint {
    protected final LoggerBlueprint logger;

    public AuthManagerBlueprint(LoggerBlueprint logger) {
        this.logger = logger;
    };

    public abstract int registerUser(String name, String password);

    public abstract int authenticateUser(String name, String password);

    public abstract String generatePasswordHash(String password);
}
