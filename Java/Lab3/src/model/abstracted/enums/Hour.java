package model.abstracted.enums;

public enum Hour {
    ONE("один"),
    TWO("два"),
    THREE("три"),
    FOUR("четыре"),
    FIVE("пять"),
    SIX("шесть"),
    SEVEN("семь"),
    EIGHT("восемь"),
    NINE("девять"),
    TEN("десять");

    private final String text;

    Hour(String text) {
        this.text = text;
    }


    @Override
    public String toString() {
        if (this == Hour.ONE) {
            return text + " час";
        } else if (this == Hour.TWO || this == Hour.THREE || this == Hour.FOUR) {
            return text + " часа";
        }
        return text + " часов";
    }
}
