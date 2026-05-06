package util.exceptions;


/**
 * Исключение для ошибок заполнения формы нового элемента коллекции
 * @author Septyq
 */
public class InvalidFormException extends Exception {
    public InvalidFormException(String message) {
        super(message);
    }
}
