package model.abstracted;

public enum Wish {
    EAT("поесть"),
    SLEEP("поспать"),
    REST("отдохнуть");

    private final String text;

    Wish(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
