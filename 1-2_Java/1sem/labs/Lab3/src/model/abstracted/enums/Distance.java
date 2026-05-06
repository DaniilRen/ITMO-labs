package model.abstracted.enums;


public enum Distance {
    ONE("один", 1),
    TWO("два", 2),
    TWELVE("двенадцать", 12),
    ONE_THOUSAND("тысяча", 1000),
    TWO_THOUSAND("две тысячи", 4000),
    FOUR_HUNDRED_THOUSAND("четыреста тысяч", 400000);


    private final String distanceText;
    private final int integerRepresentation;

    Distance(String distanceText, int integerRepresentation) {
        this.distanceText = distanceText;
        this.integerRepresentation = integerRepresentation;
    }

    public int toInteger() {
        return integerRepresentation;
    }

    @Override
    public String toString() {
        if (this == Distance.ONE) {
            return distanceText + "километр";
        }
        return distanceText + " километров";
    }
}