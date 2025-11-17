package model.exceptions;

public class StoryException extends Exception {
    public StoryException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "StoryException: " + super.getMessage();
    }
}