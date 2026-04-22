package managers;

import logging.LoggerBlueprint;

public class AuthManager extends AuthManagerBlueprint {
    public AuthManager(LoggerBlueprint logger) {
        super(logger);
    };

    public int registerUser(String name, String password) {
        return 1;
    };

    public int authenticateUser(String name, String password) {
        return 1;
    };

    public String generatePasswordHash(String password) {
        return "1";
    };
}
