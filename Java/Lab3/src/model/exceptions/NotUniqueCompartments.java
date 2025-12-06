package model.exceptions;

public class NotUniqueCompartments extends Exception {
    public NotUniqueCompartments(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "NotUniqueCompartments: " + super.getMessage();
    }
}
