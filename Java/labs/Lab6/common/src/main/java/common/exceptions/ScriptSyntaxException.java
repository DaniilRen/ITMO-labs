package common.exceptions;

/**
 * Исключение для ошибок внутри скрипта
 * @author Septyq
 */
public class ScriptSyntaxException extends Exception {
    public ScriptSyntaxException(String message) {
        super(message);
    }
}
