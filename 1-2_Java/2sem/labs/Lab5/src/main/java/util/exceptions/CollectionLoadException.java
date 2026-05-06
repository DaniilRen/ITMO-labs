package util.exceptions;

/**
 * Исключение для ошибок загрузки коллекции
 * @author Septyq
 */
public class CollectionLoadException extends Exception {
    public CollectionLoadException(String message) {
        super(message);
    }
}
