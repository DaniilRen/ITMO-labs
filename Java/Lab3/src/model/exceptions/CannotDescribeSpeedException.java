package model.exceptions;

public class CannotDescribeSpeedException extends RuntimeException {
    public CannotDescribeSpeedException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "CannotDescribeSpeedException: " + super.getMessage();
    }
}
