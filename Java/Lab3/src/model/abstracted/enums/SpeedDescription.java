package model.abstracted.enums;

public enum SpeedDescription {
    CALM("маленькая"),
    SCARY("страшная"),
    STANGE("странная");

    private final String text;

    SpeedDescription(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
