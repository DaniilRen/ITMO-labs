package model.exceptions;

public class StateFieldNotSetException extends RuntimeException {
    public StateFieldNotSetException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "StateFieldNotSetException: " + super.getMessage();
    }
}
