package model.abstracted.enums;

public enum TimeUnit {
    SECOND("сек"),
    MINUTE("мин"),
    HOUR("ч");

    private final String text;

    TimeUnit(String text) {
        this.text = text;
    }


    @Override
    public String toString() {
        return text;
    }
}
