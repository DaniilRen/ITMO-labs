package model.abstracted;


public enum Measure {
    HUGE("огромное"),
    VERY_BIG("очень большое"),
    BIG("большое"),
    NOT_SO_BIG("не так велико"),
    NOT_A_FINGER_HALF("ни на пол пальца"),
    APPROXIMATELY("около"),
    WITH_APPETITE("с аппетитом"),
    SCARY("страшный");

    private final String text;

    Measure(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
