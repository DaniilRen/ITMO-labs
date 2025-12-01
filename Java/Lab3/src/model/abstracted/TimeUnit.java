package model.abstracted;

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
