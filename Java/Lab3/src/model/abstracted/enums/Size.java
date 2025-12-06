package model.abstracted.enums;

public enum Size {
    NOT_SO_BIG("не так велико"),
    BIG("большое"),
    VERY_BIG("очень большое"),
    HUGE("огромное");

    private final String text;

    Size(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
