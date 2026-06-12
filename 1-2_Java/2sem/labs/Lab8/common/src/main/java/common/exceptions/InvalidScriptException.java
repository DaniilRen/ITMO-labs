package common.exceptions;

/**
 * Исключение для ошибок скрипта
 * @author Septyq
 */
public class InvalidScriptException extends Exception {
    public InvalidScriptException(String message) {
        super(message);
    }
}
