package model.exceptions;

public class NotUniqueCompartmentsException extends Exception {
    public NotUniqueCompartmentsException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "NotUniqueCompartments: " + super.getMessage();
    }
}
