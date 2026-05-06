package model.abstracted.enums;

public enum DistanceDescription {
    LITTLE("маленькое"),
    BIG("большое"),
    HUGE("огромное");

    private final String text;

    DistanceDescription(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
