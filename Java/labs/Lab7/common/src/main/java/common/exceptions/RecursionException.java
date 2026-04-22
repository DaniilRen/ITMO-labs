package common.exceptions;


/**
 * Исключение для рекурсии в сприптах
 * @author Septyq
 */
public class RecursionException extends Exception {
    public RecursionException(String message) {
        super(message);
    }
}
