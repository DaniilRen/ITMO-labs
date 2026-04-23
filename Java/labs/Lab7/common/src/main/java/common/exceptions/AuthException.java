package common.exceptions;

/**
 * Исключение при аутентификации пользователя
 * @author Septyq
 */
public class AuthException extends Exception {
    public AuthException(String message) {
        super(message);
    }
}

