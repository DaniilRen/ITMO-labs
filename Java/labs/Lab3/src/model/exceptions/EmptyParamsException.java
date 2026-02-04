package model.exceptions;

public class EmptyParamsException extends RuntimeException {
    public EmptyParamsException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "EmptyParamsException: " + super.getMessage();
    }
}
