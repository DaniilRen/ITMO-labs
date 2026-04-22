package common.exceptions;

/**
 * Исключение для ошибок выгрузки (записи) коллекции
 * @author Septyq
 */
public class CollectionWriteException extends Exception {
    public CollectionWriteException(String message) {
        super(message);
    }
}
