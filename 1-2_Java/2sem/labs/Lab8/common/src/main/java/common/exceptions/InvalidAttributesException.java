package common.exceptions;

/**
 * Исключение для ошибок загрузки атрибутов команд с сервера
 * @author Septyq
 */
public class InvalidAttributesException extends Exception {
    public InvalidAttributesException(String message) {
        super(message);
    }
}
