package util.exceptions;


/**
 * Исключение для обработки ошибок заполнения формы
 * @author Septyq
 */
public class InvalidFormException extends Exception {
    public InvalidFormException(String message) {
        super(message);
    }
}
