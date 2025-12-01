package model.exceptions;

public class WrongFoodException extends RuntimeException {
    public WrongFoodException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "WrongFoodException: " + super.getMessage();
    }
}
