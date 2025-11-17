package model.exceptions;

public class CharacterNotFoundException extends RuntimeException {
    public CharacterNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "CharacterNotFoundException: " + super.getMessage();
    }
}