package model.abstracted.enums;

public enum Wish {
    EAT("поесть", Feeling.HUNGER),
    SLEEP("поспать", Feeling.SLEEPY),
    REST("отдохнуть", Feeling.CALMNESS);

    private final String text;
    private final Feeling feeling;

    Wish(String text, Feeling feeling) {
        this.text = text;
        this.feeling = feeling;
    }

    public Feeling toFeeling() {
        return feeling;
    }

    @Override
    public String toString() {
        return text;
    }
}
